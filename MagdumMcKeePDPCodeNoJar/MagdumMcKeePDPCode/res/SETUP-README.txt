
Running the program from the JAR file -- 

1. Make sure the runnable JAR file and Full Ticker List.csv file are in the same directory (any will do).

2. While in that directory enter this command -: java -jar Stock_MVC.jar

3. Enter 'text' into the console to use the text ui. Enter 'gui' to use the GUI.

4. At this point you should see the welcome screen for either of the ui's.

5. If using the text ui, the program will exit when option '6' is selected from the main menu.
   If using the GUI, the program will exit when the window is closed.





Old: Instructions to purchase 3 companies stock on 3 different dates and view the value and cost basis on 2 different dates:

1. User will see ' Welcome to 'GROW MONEY'! ' screen after running the JAR file.

2. Enter '2' to navigate to the build/edit menu

3. Enter '2' to begin making a flexible portfolio

4. Enter any non-empty alphanumeric string as the portfolio's name: 'example'

5. To buy a stock, enter: 'b'

6. Enter the stock's ticker: 'MSFT'

7. Enter the count: '100'

8. Enter the date in 3 separate inputs (each input will be individually queried): '2015' '01' '01'

9. Repeat 5-8 with the following inputs:
    'b' 'AAPL' '200' '2012' '05' '24'
    'b' 'GOOG' '150' '2010' '10' '01'

10. Enter 'done' to finish changing the portfolio, then enter anything to return to the build/edit menu

11. Enter '4' to return to the main menu

12. Enter '3' to navigate to the view menu

13. Enter '2' to see the portfolio's value on a certain date

14. Enter '1' to select 'example'

15. Repeat step 8: '2011' '01' '01'

16. Wait patiently for the API, try to resist entering additional input, it may confuse the menus and the user
    (it shouldn't cause any problems though)

17. Enter anything to continue, then repeat steps 13-16 with a different date: '2016' '05' '05'

18. Enter anything to continue, then enter '3' to view the cost basis of a portfolio

19. Enter '1' to select 'example'

20. Repeat step 8: '2011' '01' '01'

21. Repeat step 16

22. Enter anything to continue, then repeat steps 18-21 with a different date: '2016' '05' '05'

23. Enter anything to continue, then enter '6' to return to the main menu

24. (Optional) Enter '4' then '1' or just enter '5' to save 'example'

25. Enter '6' to exit the program

