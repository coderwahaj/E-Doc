E-Doc is an Android application developed using Java and Firebase to facilitate efficient communication between doctors and patients. The application provides a platform for patients to consult with doctors, schedule appointments, share medical reports, and receive prescriptions digitally. It ensures a streamlined and secure communication channel, improving the overall healthcare experience.
Features :
User Authentication: Secure login and registration for both doctors and patients using Firebase Authentication.
Profile Management: Users can create and manage their profiles, including personal details, medical history, and more.
Appointment Scheduling: Patients can book appointments with available doctors based on their schedules.
Chat Functionality: Real-time chat between doctors and patients for consultations and follow-ups.
Medical Records: Patients can upload and share their medical records with doctors.
Prescription Management: Doctors can send prescriptions directly to patients through the app.
Notifications: Push notifications to keep users informed about appointments, messages, and updates.
Secure Data Storage: All user data is securely stored and managed using Firebase Firestore and Firebase Storage.
Installation
Clone the Repository

bash
Copy code
git clone https://github.com/coderwahaj/e-doc.git
Open in Android Studio

Open Android Studio.
Select Open an existing Android Studio project.
Navigate to the cloned repository and select it.
Configure Firebase

Go to the Firebase Console.
Create a new project or use an existing one.
Add your Android app to the Firebase project.
Download the google-services.json file and place it in the app directory of your project.
Enable Firebase Authentication, Firestore, and Storage in the Firebase Console.
Build and Run

Sync the project with Gradle files.
Build the project and run it on an Android device or emulator.
