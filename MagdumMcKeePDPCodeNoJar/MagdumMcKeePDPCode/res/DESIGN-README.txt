Changelog:
1. ControllerImpl was created to migrate the controller to a command structure.
Why: we wanted to cut down on the size of the controller, which was growing larger and larger and
the command pattern allowed segregation of code by function

2. PortfolioController, PortfolioControllerImpl, and InputControllerImpl were removed.
Why: they were unnecessary with the new command structure.

3. Command, TextCommand, GuiCommand, and the packages textcoms and guicoms were created and filled
   with the existing code.
Why: for the command pattern controller

4. Strategy and DCAStrategy were added.
Why: to persist strategies added to a portfolio

5. addStrategy and updateFromStrategy were added to PortfolioManager to allow the application
   of strategies to portfolios.
Why: to accommodate the assignment requirements

6. A new interface GuiInterface was created extending the ViewInterface.
Why: we needed to add a GUI, and the GUI required all the same methods and several more

7. Persistence was updated for strategies
Why: to persist strategies


APPLICATION DESIGN --
The program's SRC folder structure is below (we also list our test files) -

Stocks_MVC
  src
    controller
      API
      APIImpl
      Command
      TextCommand
      GuiCommand
	    InputController
	    ControllerImpl
	    Persistence
	    PersistenceInterface
    model
	    FlexPortfolioImpl
	    FlexStock
	    Persistence
	    Portfolio
	    PortfolioImpl
	    PortfolioManager
	    PortfolioManagerImpl
	    Stock
	    StockInterface
	    Strategy
	    DCAStrategy
    view
      GuiInterface
      JFrameView
	    ViewImpl
	    ViewInterface
  test
    FlexPortfolioImplTest
    FlexStockTest
    ControllerImplTest
    TextCommandTests
    GuiCommandTests
    StrategyTest
    PortfolioImplTest
    PortfolioManagerImplTest
    StockTest
    ViewImplTest


Overall Design:
We follow an MVC architecture.
    The VIEW now has two interfaces. The main ViewInterface, which the ViewImpl (text) implements.
    And, the GuiInterface which extends the ViewInterface and is implemented by JFrameView (GUI).
    The view receives its commands from the commands in the controller package, and does not talk
    to the model.

    The MODEL is primarily represented by the 'Portfolio Manager' which maintains a list of
    Portfolios (which themselves have lists of Stock objects and Strategy objects) and is
    responsible for the building, editing, and querying of portfolios. The model is passed a
    persistence object by the controller in construction, and is also passed an API object when
    using methods that value portfolios (e.g. cost basis). The portfolios themselves only have
    methods to get their own name and the values held by their stock objects. The stock objects
    only have methods to return the data within; these objects take generic data, but are created
    with <String, Float> or <String, Float, Date>. The model only "speaks" to the controller.

    The CONTROLLER now follows a command structure and has maps that identify the proper command
    based on state-variables like the UI of choice and the current screen. The command objects
    themselves only have one method a-piece called 'go()'. The other role of the controller is
    setting up those maps, setting up the view and the model, and communicating between the model
    and the view. The command objects are found in the textcoms and guicoms subpackages.

    The flexible portfolio has two rules to keep in mind while constructing it. You cannot sell
    stock you have not already bought in the portfolio. You cannot sell stock on a date before if
    not enough of that stock was previously purchased.

    The strategies cannot be applied when there are no stocks in the portfolio. This is somewhat
    fudged during the function to build a portfolio from scratch with a strategy, but is still
    mostly true. Adding a strategy also requires that all stocks are available by the starting
    date.