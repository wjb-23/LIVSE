# Local Image and Video Search Engine (LIVSE)
*James Bradley and Tenzin Dayoe*


LIVSE is an application that stores a user’s images efficiently using an optimized data structure and image/video categorization ML model. 

Users can search for their images using tags—the names of objects detected in a given image or video—which are determined by the captioning ML model in a matter of seconds. The app’s performance stays relatively high for larger image/video datasets.

All search operations (other than queries involving a file's associated date) are handled by the *GenHashMap* class, while date-related searches are handled by *DateTreeMap*. 

Given that each image has a constant number of tags, *t* (constant), and that a data structure (either tree or hash map) has *n* files stored:

- The time complexity of its sorting operations is mostly ~O(t×n) -> O(n) at worst (the absolute worst being O(t×n×m) -> O(n×m) where *m* is the total number of unique tags generated by the ML models).
- For most general use cases, the time complexity would be ~O(t×1) -> O(1). 

With the *OperationStats* feature, users can view the speed of their past search queries and compare them.

When a user submits the image/video through the GUI:
- The Java client sends the image/video path to the python socket server (Client.java).
- The image/video path will be in the software documents folder that is created in the system document folder.
- The ML algorithm processes the image/video and sends the corresponding tags/predictions result to the java client (ObjectDetection.py). 

# Object Detection Models - PyTorch

Image Recognition
YOLOv5
  - DOI: https://zenodo.org/badge/latestdoi/264818686

Video Recognition
3D ResNet
  - https://pytorch.org/hub/facebookresearch_pytorchvideo_resnet/

# Modules and Use

The application works best on Windows OS, however, it should be possible to launch the ObjectDetection.py server through the terminal on Mac OS.

First, create a new folder named 'Viste' in your system documents folder. This is where files will be placed when downloaded through the Application.

Then open the system terminal and enter the following commands in order:

- 'cd documents'
- 'cd github'
- 'cd 128-project-james-dayoe-project'
- 'python3 ./src/ObjectDetection.py ./Users/user/Documents/Viste/'
\(replace 'user' with the corresponding identifier in your path)

Lastly, run MainUI.java and the application should appear. The 'add folder' button may not work on Mac OS.

While there is a module installation script that runs on startup in ObjectDetection.py, you can install these python packages manually to ensure functionality:

- numpy
- pandas
- pyyaml
- seaborn
- tqdm
- fvcore
- av
- psutil
- matplotlib
- torch
- opencv-python
- torchvision

