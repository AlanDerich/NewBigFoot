The main technologies used are Android Studio, Kotlin, Jetpack Compose, and Firebase. 
All the data is stored in the Firebase database and fetched using Flow functionality. 
Authentication is made through the user’s phone number and an OTP is sent to their phone. The member’s phone number must be added by an administrator in the database in order to view the group information.
The project was an inspiration for a group I am in where we used to save 20 KSH per day before changing to 35 KSH per day. The group started in 2020 and we started with a simple book where we recorded all the transactions.
As the transactions increased, it became even harder to keep track of the transactions made and the current member contributions. It became necessary to find a better way to manage the data. We later started the loans project where each member can borrow a loan equivalent to 75% of their total savings.

All the users use Android apps, and an Android App was suitable for this case. The Android App was meant to serve the following purposes:

   **1. Keep track of each transaction made (To be updated by the admin)**
   
   **2. Show the members their status and arrears.**
   
   **3. Show the loans taken so far and the status of those loans (repaid or not).**
    
Since the app has very few members, I decided to use Firebase which provides free storage and authentication. The completed app has four screens.

  **1. Display of member details (Home)**
     
Here, details such as the total group savings, the logged-in member's current total savings and the date these savings cover are displayed. Also, the screen shows a list of all the members’ current contributions status in descending order from the highest amount to the lowest.

![home dark](https://github.com/AlanDerich/NewBigFoot/assets/50056881/04abb940-7866-4099-b4d5-841d5451dd27)
![home light](https://github.com/AlanDerich/NewBigFoot/assets/50056881/3412f6f3-2da7-4594-835e-a5e7ee78833d)
![home dark landscape](https://github.com/AlanDerich/NewBigFoot/assets/50056881/1af6b11b-906d-47b6-ab41-f11cecc67363)
![home light landscape (2)](https://github.com/AlanDerich/NewBigFoot/assets/50056881/6b371f54-8988-4c7c-bb91-c21cee3df317)


 **2. Transactions**
     
This page contains a list of all transactions. At the top of the page is a search bar where users can search through the list for specific transactions.

![transactions dark](https://github.com/AlanDerich/NewBigFoot/assets/50056881/b5a29fc8-4bf4-409c-9167-210a6bbac0b8)
![transactions-light](https://github.com/AlanDerich/NewBigFoot/assets/50056881/3a61bf46-1d2d-4fc1-99ed-26b26151478d)
![transactions dark landscape](https://github.com/AlanDerich/NewBigFoot/assets/50056881/893d0900-901f-4ff0-9434-5dcb6572bb15)
![transactions light landscape](https://github.com/AlanDerich/NewBigFoot/assets/50056881/f2641dac-8b15-43c7-b440-31276ead8f6d)


 **3. Loans**
     
This page displays information on the loan project. It has a list of all loans and the status of each loan. At the top of the page is a button that users can click in order to view all statistics such as the Initial amount invested in the project, the total loans given so far, the total loans repaid, and outstanding loans. It also shows the profits made so far among other key details.

 **4. Account**

The page displays the logged-in member details including a profile picture, User full name, phone number, and buttons to log out and request deletion of their account. Users also have the option to update their profile pictures.

![account dark](https://github.com/AlanDerich/NewBigFoot/assets/50056881/1ed1bd14-3aaa-48ac-97e8-83d202052306)
![account light](https://github.com/AlanDerich/NewBigFoot/assets/50056881/5c23aa8c-6639-44a4-b06d-4675abe0172b)
![account dark landscape](https://github.com/AlanDerich/NewBigFoot/assets/50056881/36b1e565-bae3-4127-9593-6d26acee4905)
![account light landscape](https://github.com/AlanDerich/NewBigFoot/assets/50056881/79e2f254-775b-4c9d-b691-1474c3b2abff)


The app has other features if an admin is logged in such as adding transactions and exporting all the database details for backup purposes.
