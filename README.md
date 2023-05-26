# radiorandgen
Random number generator based on the digital stream of Internet radio
We will read the Internet radio stream and look for some kind of sequence of, for example, 16 digits in it. Then we write down the next N numbers, which will be random and which we use for the Vernam cipher as the encryption key.
First app prototype. Listens to an internet radio stream and finds a given sequence of bytes
This is a minimal project that shows in LOG how often a given sequence of bytes can be found in an internet radio digital stream.
Next, you need to write as many bytes of the stream as you need to encrypt the record using the Vernam algorithm
There are several options for obtaining entropy. Let's leave this project as a minimal prototype and create a new project RADIRANDGEN2, in which we will receive a cipherbook page from the stream and add the function of synchronizing the reading of entropy from the stream from the sender of the message and the recipient of the message without exchanging information between them.
