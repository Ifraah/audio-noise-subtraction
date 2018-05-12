# audio-noise-subtraction

 
Application of Spectral Subtraction for Suppression of Acoustic Noise 


INTRODUCTION:

The project is the implementation of a  standalone, noise- suppression algorithm for reducing the spectral effects of acoustically added noise.Noise acoustically added to speech can degrade the performance of digital voice processors used for applications such as speech compression, recognition, and authentication. In order to ensure, continued reliability, the effects of background noise can be reduced. This project focuses on overcoming this problem.The objective of this project is to develop a noise suppression technique, implement a computationally efficient algorithm, and test it’s performance. The method used suppresses stationary noise from speech by subtracting the spectral noise bias calculated during non speech activity.


METHOD:

First take sound wave and add random noise to it. Next, estimate the frequency of the underlying clean speech by subtracting the noise magnitude spectrum from the noisy speech spectrum. This estimator requires an estimate of the current noise spectrum. The noise estimate spectrum can be obtained by either using average noise magnitude during non-speech activity or subtracting a random noise magnitude spectrum from the noisy speech spectrum. This app implements the latter of the two.
