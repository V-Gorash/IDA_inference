import argparse
import atexit
import base64
import io
import requests
import numpy as np
import tensorflow as tf
import cv2

from PIL import Image
from flask import Flask, request, Response
from flask_consulate import Consul

app = Flask(__name__)


@app.route("/segmentation", methods=["POST"])
def segmentation():
    global model
    image_base64 = request.form.get("image")
    image = Image.open(io.BytesIO(base64.b64decode(image_base64)))
    temp = cv2.cvtColor(np.asarray(image), cv2.COLOR_GRAY2BGR)
    input_arr = cv2.cvtColor(temp, cv2.COLOR_BGR2GRAY)
    clahe = cv2.createCLAHE(clipLimit=7.0)
    input_arr = clahe.apply(input_arr)
    input_arr = np.array(input_arr).reshape((512, 512, 1)) / 255
    result = model.predict(np.array([input_arr]))
    res_image = tf.keras.utils.array_to_img(result[0])
    buffered = io.BytesIO()
    res_image.save(buffered, format="PNG")
    image_base64 = base64.b64encode(buffered.getvalue())
    return Response(image_base64, mimetype='text/plain')


@app.route("/healthcheck")
def health_check():
    return '', 200


def on_exit(service_id):
    requests.put("http://localhost:8500/v1/agent/service/deregister/" + str(service_id))


consul = Consul(app=app)
consul.apply_remote_config(namespace='mynamespace/')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="IDA image segmentation service",
                                     formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument("-p", "--port")
    parser.add_argument("-i", "--ip")
    parser.add_argument("-m", "--model")

    args = parser.parse_args()
    config = vars(args)

    if config["port"] is not None:
        port = int(config["port"])
    else:
        port = 15080

    if config["ip"] is not None:
        host = config["ip"]
    else:
        host = "localhost"

    assert config["model"] is not None, "Error: empty model path"

    consul.register_service(
        name='IDA-Segmentation',
        service_id=str(port),
        interval='1s',
        port=port,
        httpcheck='http://' + host + ':' + str(port) + '/healthcheck'
    )
    atexit.register(lambda: on_exit(port))
    model = tf.keras.models.load_model(config["model"], custom_objects={'dice_loss': lambda: 0, 'dice': lambda: 0})
    app.run(port=port, host=host)
