# Project5
Purdue CS180 Project 5

Instructions on how to compile and run your project. 

Run the main method in the GUI class and follow the instructions as the console prompts you to do so.

A list of who submitted which parts of the assignment on Brightspace and Vocareum. 

Student 1 - Submitted Report on Brightspace. 
Krish Patel - Submitted Vocareum workspace.

A detailed description of each class. This should include the functionality included in the class, the testing done to verify it works properly, and its relationship to other classes in the project. 


User.java - This is the genral class that will be extended by both the seller and the consumer, it will have a few genral methods that can be used
by both of is child classes. This class includes a constructor that makes a unique file for each new User. The methods in User handle the message sending, delting, editing, printing/displaying as well as exporting, and importing files. 

Consumer.java / Seller.java: 2 child classes of User one represents sellers and one represents consumers, both sellers and consumers have methods that have super calls to User with slightly diffrent information passed in the parametes

BlankClient.java - This class handles the client side of our operations, showing a proper GUI 

BlankServer.java - 

BlankServerThread.java - 

For GroupMates: 
File Structure For Project: 
Will Have 3 Different Types of Files (Consumer Information, Seller Information, Text File)

Consumer Information: - All File Names will be ConsumerName.txt ex (ConsumerJim.txt)
1. Account Name
2. Password
3. Blocked Users - ex: "Max, Sam, Luke"
4. This line and all lines under are names of text files and their corresponding Seller (ex: "Max: MaxConvo.txt")

Producer Information: - All File Names will be ProducerName.txt ex (ProducerJim.txt)
1. Account Name
2. Password
3. Blocked Users - ex: "Max, Sam, Luke"
4. Stores - ex: "Macy's, H&M, Marshalls"
5. This line and all lines under are names of text files and their corresponding Seller (ex: "Max: MaxConvo.txt")

Text File:
1. "Producer: Hey Man"
2. "Consumer: How are you doing"
3. "Producer: Good?"
4. etc. 
