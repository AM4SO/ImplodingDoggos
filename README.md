
# ExplodingKittens
Some Exploding Kittens/ImplodingDoggos stuff. 

Assessment recordings
----------------------------------------------

End of iteration 4 testing: https://user-images.githubusercontent.com/79862724/235636376-4ce8d04f-5cda-4f2e-ad6d-f8acc48da43e.mp4

Post-developmental testing - Test 2: https://user-images.githubusercontent.com/79862724/236680894-49928bdf-0836-4f18-8c64-76d13612c2d3.mp4

About this project
---------------------------------------------
This project is an implementation of the card game 'Exploding Kittens'. I started this project as my A-level Computer Science programming project because none of the other Exploding Kittens implementations on the Play Store are authentic implementations of the card game; They are always missing certain game mechanics. For example, they don't let players place down 'Nope' cards, which are a huge part of the game, to stop other players, or play card combos.

The plan for this project was to make a networked card game, where the server program can run on its own, or on the client machine (an Android device). For this to work, I determined that the best approach would be to use Java for both the client and the server, as it should allow me to program the server once, and have it run on either the Android device or a PC. 

The project would consist of a server-side and a client-side. The server side would include a 'gameserver' application which controls the flow of the game, and accepts TCP connections so players can connect to the game. It would also include an HTTP server which would allow players to request to join a public game instance, and respond with the details for the player to connect to that game. 

The client-side would just include an Android application which allows the players to interface with the gameserver. 

-------------------------------------------------------
Unfortunately, due to time constraints, I wasn't able to finish making this game. I managed to complete the base networking side of the project to allow the client and server to have the ability to send any type of message about what is happening in the game. I had a running game loop, but only some of the basic cards had been implemented. I initially wanted to continue working on the server-side to get the game logic completed, however as my time was running out and I needed something presentable to show the OCR exam board, I had to move onto the Android app. During this stage, it became clear that using Android Studio was quite a suboptimal way to go, given that I wanted to animate the game quite substantially, had never developed for Android before, and didn't prefer front-end development. As a result, the development of the client side became very time-consuming, and at some point, I had to stop so I could try to focus on exams. 
