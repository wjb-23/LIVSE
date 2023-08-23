import sys
import os
import subprocess
import json
import urllib
import socket
from sys import platform
import subprocess 

# Python script for handling requests (file paths) from Client.java and generating responses (list of object tags).

command = '''
on run argv
  display notification (item 2 of argv) with title (item 1 of argv)
end run
'''

def createNotificationMac(notfTitle, notfText):
  subprocess.call(['osascript', '-e', command, notfTitle, notfText])




try:
    from plyer.utils import platform
    from plyer import notification
except :
    if(sys.platform.startswith("win32")):
        p = subprocess.Popen(['pip','install','plyer'])
        notification.notify(
        title='Installing Dependencies',
        message='Installing Plyer...',
        app_name='Python',
        app_icon=None
        )


    
#package instation/imports
lib_list = ['numpy','pandas','pyyaml','seaborn','tqdm','fvcore','av','psutil','matplotlib','torch','opencv-python', 'torchvision']
def notify(mes):
    
    if (sys.platform.startswith("win32")):
        subprocess.Popen(['pip','install',mes])
        notification.notify(
            title='Installing Dependencies',
            message='Installing '+mes,
            app_name='Python',
            app_icon=None
        )
    else :
        subprocess.Popen(['pip3','install',mes])
        createNotificationMac("Installing Dependencies", "Installing" + mes)
try:
    import numpy
except:
   notify('numpy')

try:
    import pandas
except :
    notify('pandas')

try:
    import cv2
except : 
    notify('opencv-python')

try:
    import psutil
except:
    notify('psutil')

try:
    import yaml
except:
    notify('pyyaml')

try:
    import tqdm
except:
    notify('tqdm')

try:
    import matplotlib
except:
    notify('matplotlib')
try:
    import fvcore
except:
    notify('fvcore')

try:
    import av
except:
    notify('av')

try:
    import torch
except:
    notify('torch')
try:
    import torchvision
except:
    notify('torchvision')


import torch
import cv2
import pandas


model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)
vidmodel = torch.hub.load('facebookresearch/pytorchvideo', 'slow_r50', pretrained=True)


from pytorchvideo.data.encoded_video import EncodedVideo

from torchvision.transforms import Compose, Lambda
from torchvision.transforms._transforms_video import (
    CenterCropVideo,
    NormalizeVideo,
)
from pytorchvideo.transforms import (
    ApplyTransformToKey,
    ShortSideScale,
    UniformTemporalSubsample
)

device = "cpu"
vidmodel = vidmodel.eval()
vidmodel = vidmodel.to(device)

json_url = "https://dl.fbaipublicfiles.com/pyslowfast/dataset/class_names/kinetics_classnames.json"
json_filename = "kinetics_classnames.json"
try: urllib.URLopener().retrieve(json_url, json_filename)
except: urllib.request.urlretrieve(json_url, json_filename)

with open(json_filename, "r") as f:
    kinetics_classnames = json.load(f)

# Create an id to label name mapping
kinetics_id_to_classname = {}
for k, v in kinetics_classnames.items():
    kinetics_id_to_classname[v] = str(k).replace('"', "")


# write a method here for this 




side_size = 256
mean = [0.45, 0.45, 0.45]
std = [0.225, 0.225, 0.225]
crop_size = 256
num_frames = 8
sampling_rate = 8
frames_per_second = 30

# Note that this transform is specific to the slow_R50 model.
transform =  ApplyTransformToKey(
    key="video",
    transform=Compose(
        [
            UniformTemporalSubsample(num_frames),
            Lambda(lambda x: x/255.0),
            NormalizeVideo(mean, std),
            ShortSideScale(
                size=side_size
            ),
            CenterCropVideo(crop_size=(crop_size, crop_size))
        ]
    ),
)



# The duration of the input clip is also specific to the model.
clip_duration = (num_frames * sampling_rate)/frames_per_second


#ends here .........




if __name__ == "__main__":
    print(sys.argv)
    dList = sys.argv[1:len(sys.argv)]
    directory =""
    if len(dList) >1:
        directory = dList[0] + " " + dList[1]
    
    else:
        directory = dList[0]
    directory = directory.replace(".","")
    os.chdir(directory)  

# Make predictions on the image using the YOLOv5 model
def pathProcessor(p):
    if ".mp4" in p : 
        return "vid"
    if ".jpg" in p or ".jpeg" in p or ".png" in p:
        return "img"
    
def detect(path):
    finList = []
    fileFormat = pathProcessor(path)

    if fileFormat == "vid":

        video_path = path
        start_sec = 0
        end_sec = start_sec + clip_duration

        # Initialize an EncodedVideo helper class and load the video
        video = EncodedVideo.from_path(video_path)

        # Load the desired clip
        video_data = video.get_clip(start_sec=start_sec, end_sec=end_sec)

        # Apply a transform to normalize the video input
        video_data = transform(video_data)

        # Move the inputs to the desired device
        inputs = video_data["video"]
        inputs = inputs.to(device)
        preds = vidmodel(inputs[None, ...])

        # Get the predicted classes
        post_act = torch.nn.Softmax(dim=1)
        preds = post_act(preds)
        pred_classes = preds.topk(k=5).indices[0]


        # Map the predicted classes to the label names
        pred_class_names = [kinetics_id_to_classname[int(i)] for i in pred_classes]
        
        if len(pred_class_names)>=5:
            pred_class_names = pred_class_names[0:5]
        temp = []
        for s in pred_class_names:
            temp.append(s.lower().replace(" ",""))
        finList = temp
                

    elif fileFormat == "img":

        image_path = path
        image = cv2.imread(image_path)
        results = model(image)
        for result in results.xyxy[0]:
            xmin, ymin, xmax, ymax, confidence, class_id = result
            class_name = model.names[int(class_id)]
            if class_name not in finList:
                if confidence >0.66:
                    n = class_name.lower().strip()
                    finList.append(n)
            print(f"{class_name}: {confidence}")
        finList = finList[0:5]
    if len(finList) == 0:
        finList = ["Na"]
    return finList
# Define the host and port to listen on
HOST = 'localhost'
PORT = 5000

# Create a socket object
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to a specific address and port
sock.bind((HOST, PORT))

# Listen for incoming connections
sock.listen()

def handle_conn(conn,addr):
    # Wait for a connection
    
    print("connected : Address = " +str(addr))
    if (sys.platform.startswith("win32")):

        notification.notify(
            title='Python Server',
            message='Connected to Java Client',
            app_name='Python',
            app_icon=None
        )

    else:
        createNotificationMac("Python Server", "Connected to Java Client.")

    data = conn.recv(1024)
    
    imageName = data.decode('utf-8')
    imageName = imageName[2::]



    resp = detect(imageName)
    print(resp)
    object_names = ','.join(resp)

    # Send the object names to the client
    encoded_data = object_names.encode('utf-8')
    print(encoded_data)
    conn.sendall(encoded_data)

    conn.close()
    # Send a list of strings to the client
    

while True:
    conn, addr = sock.accept()
    handle_conn(conn, addr)


 