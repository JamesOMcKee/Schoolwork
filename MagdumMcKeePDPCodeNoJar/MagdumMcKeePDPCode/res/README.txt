Features of the program that are completely working --

All listed features are working the Text UI. Features that also work in the GUI have '(GUI)' next to them.
NOTE: The GUI was designed on a single screen. We found right before submission that different devices
might skew the elements in the GUI. Please resize your window if you have this issue.

1. User is able to see the menu and select the operation by his choice. Operations shown to the user are (names of operations may vary) -
Main Menu
	1. Load a portfolio (GUI)
		1. Enter filename to load
		2. Go to previous menu

	2. Build a portfolio (GUI)
		1. Begin building a simple portfolio
		2. Begin building a flexible portfolio (GUI)
		3. Begin building a flexible portfolio with a strategy (GUI)
		4. Edit a flexible portfolio (GUI)
		5. Add a strategy to an existing flexible portfolio (GUI)
		6. Add a fixed cost buy to an existing flexible portfolio (GUI)
		7. Go to previous menu

	3. View a portfolio (GUI)
		1. View the stocks list in a portfolio (GUI)
		2. View the value of a portfolio on a certain date (GUI)
		3. View the cost basis of a flexible portfolio (GUI)
		4. View the performance over time for a flexible portfolio
		5. View the value of a portfolio with manually inputted values
		6. Go to previous menu

	4. Save Portfolios (GUI)
	  1. Save a portfolio (GUI)
    2. Save all portfolios (GUI)

	5. Exit from the application

2. The features that are working option-wise -

	a. Load Portfolio
		1. User is able to load the file into code from local system.
		2. After loading the screen displays the contents of loaded portfolio.
		3. User is able to navigate back to main menu.

	b. Build Portfolio
		1. User is able to give a name for the portfolio in order to distinguish between different portfolios.
		2. User is able to enter a ticker number and count of stocks for various tickers for multiple number of times.
		3. For ticker validation, user gets a warning if invalid ticker is entered.
		4. If the portfolio being built is flexible, the user starts by choosing buy or sell, then enter tickers and
		    counts, then finally enter the date it was purchased.
		4. User can exit entering the contents of portfolio by entering 'Done'.
		5. The user may edit flexible portfolios by adding buys and sells as above
		6. When the user enters 'done' the contents of the portfolio will be shown
		7. User may build a flexible portfolio from scratch by adding and immediately applying a strategy
		8. User may add a new strategy to an existing flexible portfolio, as long as the portfolio has stocks
		9. User may execute a fixed cost buy across stocks in the portfolio, much like a strategy but only for a single date
		10. User can navigate back to the main menu.

	c. View Portfolio
		1. User can view contents for the created portfolios.
		2. User can find value of a portfolio by a certain date from 1990 to the current date.
		       NOTE - for option 2 the price used is the adjusted closing price.
        3. The user may request a cost basis for a flexible portfolio on a given date.
        4. The user may request the performance over time for a flexible portfolio between two given dates.
		5. User has an option to enter stock prices manually and then view portfolio contents.
		6. User can navigate back to the main menu.

	d. Save Portfolio
		1. User can select and save a portfolio in .csv file.
		2. User may choose to save all portfolios stored in the model.

	e. Save All Portfolios
		1. Save all the portfolios created by the user at once in their respective .csv files.
