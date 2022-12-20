import argparse
import atexit
import base64
import io
import requests
import numpy as np
import tensorflow as tf

from PIL import Image
from flask import Flask, request, Response
from flask_consulate import Consul

app = Flask(__name__)


@app.route("/check", methods=["POST"])
def check():
    global model
    image_base64 = request.form.get("image")
    image = Image.open(io.BytesIO(base64.b64decode(image_base64)))
    input_arr = tf.keras.utils.img_to_array(image)
    input_arr = np.array([input_arr])
    result = model.predict(input_arr).argmax(axis=-1)[0]
    return Response(str(result), mimetype='application/json')


@app.route("/healthcheck")
def health_check():
    return '', 200


def on_exit(service_id):
    requests.put("http://localhost:8500/v1/agent/service/deregister/" + str(service_id))


consul = Consul(app=app)
consul.apply_remote_config(namespace='mynamespace/')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="IDA image check service",
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
        name='IDA-Check',
        service_id=str(port),
        interval='30s',
        port=port,
        httpcheck='http://' + host + ':' + str(port) + '/healthcheck'
    )
    atexit.register(lambda: on_exit(port))
    model = tf.keras.models.load_model(config["model"])
    app.run(port=port, host=host)
