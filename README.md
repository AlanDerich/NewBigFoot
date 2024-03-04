An Android project showcasing my ability to build Android Apps.
The main technologies used are Android Studio, Kotlin, Jetpack Compose, and Firebase. All the data is stored in the Firebase database and fetched using Flow functionality. Authentication is made through the user’s phone number and an OTP is sent to their phone. The member’s phone number must be added by an administrator in the database in order to view the group information.
The project was an inspiration for a group I am in where we used to save 20 KSH per day before changing to 35 KSH per day. The group started in 2020 and we started with a simple book where we recorded all the transactions.
As the transactions increased, it became even harder to keep track of the transactions made and the current member contributions. It became necessary to find a better way to manage the data. We later started the loans project where each member can borrow a loan equivalent to 75% of their total savings.
All the users use Android apps, and an Android App was suitable for this case. The Android App was meant to serve the following purposes:
Keep track of each transaction made (To be updated by the admin)
Show the members their status and arrears.
Show the loans taken so far and the status of those loans (repaid or not).
Since the app has very few members, I decided to use Firebase which provides free storage and authentication. The completed app has four screens.
1. Display of member details (Home)
Here, details such as the total group savings, the logged-in member's current total savings and the date these savings cover are displayed. Also, the screen shows a list of all the members’ current contributions status in descending order from the highest amount to the lowest.
![IMG-20240304-WA0018](https://github.com/AlanDerich/NewBigFoot/assets/50056881/64d61b2a-dfe0-4bb7-8c0e-5f0b0d98aedf)
![IMG-20240304-WA0011](https://github.com/AlanDerich/NewBigFoot/assets/50056881/1d1bfba0-ea90-43cc-b152-dd63b732b59f)
![IMG-20240304-WA0012](https://github.com/AlanDerich/NewBigFoot/assets/50056881/e713d8b3-6af7-4a9d-8276-38bddd99828c)
![IMG-20240304-WA0014](https://github.com/AlanDerich/NewBigFoot/assets/50056881/53c7abbf-662c-4998-af94-9ceb5d100367)

2. Transactions
This page contains a list of all transactions. At the top of the page is a search bar where users can search through the list for specific transactions.
![IMG-20240304-WA0041](https://github.com/AlanDerich/NewBigFoot/assets/50056881/57da9eee-1b2b-4e0a-883e-dd355269f94e)
![IMG-20240304-WA0042](https://github.com/AlanDerich/NewBigFoot/assets/50056881/08a33166-09b8-4cfb-9b13-0e7f32a6a1ca)
![IMG-20240304-WA0043](https://github.com/AlanDerich/NewBigFoot/assets/50056881/187631e7-2b79-4984-934f-2cfe1a96bccd)
![IMG-20240304-WA0044](https://github.com/AlanDerich/NewBigFoot/assets/50056881/3f178264-5d96-43fb-ae60-a2c38872fdc7)

4. Loans
This page displays information on the loan project. It has a list of all loans and the status of each loan. At the top of the page is a button that users can click in order to view all statistics such as the Initial amount invested in the project, the total loans given so far, the total loans repaid, and outstanding loans. It also shows the profits made so far among other key details.
5. Account
The page displays the logged-in member details including a profile picture, User full name, phone number, and buttons to log out and request deletion of their account. Users also have the option to update their profile pictures.
![IMG-20240304-WA0010](https://github.com/AlanDerich/NewBigFoot/assets/50056881/c485dde5-5069-4449-9984-746316b804a3)
![IMG-20240304-WA0013](https://github.com/AlanDerich/NewBigFoot/assets/50056881/8ddcd517-adb4-4eab-93a2-e477b8e158e9)
![IMG-20240304-WA0016](https://github.com/AlanDerich/NewBigFoot/assets/50056881/92c6d3d4-435a-420f-8735-75a477a32c65)
![IMG-20240304-WA0017](https://github.com/AlanDerich/NewBigFoot/assets/50056881/df6f3641-288c-4bf0-93c4-484c5308141d)

The app has other features if an admin is logged in such as adding transactions and exporting all the database details for backup purposes.
