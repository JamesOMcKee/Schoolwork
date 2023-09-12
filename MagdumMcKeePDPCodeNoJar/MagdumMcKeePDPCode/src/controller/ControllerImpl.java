package controller;

import controller.guicoms.BuildEditCommandGui;
import controller.guicoms.BuildFlexibleCommandGui;
import controller.guicoms.CostBasisGuiCommand;
import controller.guicoms.DollarCostBuyGuiCommand;
import controller.guicoms.DollarCostStartCommand;
import controller.guicoms.EditFlexibleCommandGui;
import controller.guicoms.LoadCommandGui;
import controller.guicoms.PortfolioValueGuiCommand;
import controller.guicoms.SaveCommandGui;
import controller.guicoms.SaveGuiCommand;
import controller.guicoms.StrategyGuiCommand;
import controller.guicoms.StrategyValidateInfoGuiCommand;
import controller.guicoms.StrategyValidateStockGuiCommand;
import controller.guicoms.ViewCommandGui;
import controller.guicoms.ViewContentsGuiCommand;
import controller.textcoms.BuildFlexibleCommand;
import controller.textcoms.BuildSimpleCommand;
import controller.textcoms.CostBasisCommand;
import controller.textcoms.DollarCostBuyCommand;
import controller.textcoms.EditFlexibleCommand;
import controller.textcoms.LoadCommand;
import controller.textcoms.ManualValuationCommand;
import controller.textcoms.PortfolioPerformanceCommand;
import controller.textcoms.PortfolioValueCommand;
import controller.textcoms.SaveAllCommand;
import controller.textcoms.SaveCommand;
import controller.textcoms.StrategyBuildCommand;
import controller.textcoms.StrategyCommand;
import controller.textcoms.ViewContentsCommand;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.PortfolioManager;
import model.PortfolioManagerImpl;
import view.GuiInterface;
import view.JFrameView;
import view.ViewImpl;
import view.ViewInterface;

import javax.swing.JFrame;

/**
 * The controller for the program. It also contains Java's dictated main method.
 */
public class ControllerImpl implements InputController, ActionListener {
  private String ui;
  private String currentScreen;
  private String currentButton;
  private ViewInterface v;
  private JFrameView f;
  private PortfolioManager p;
  private Scanner sc;
  private API api;
  private boolean flag = true;
  private Map<String, Map<String, Map<String, Command>>> uiMap = new HashMap<>();

  private Map<String, Map<String, Command>> textMenus = new HashMap<>();
  private Map<String, Map<String, Command>> guiMenus = new HashMap<>();

  private Map<String, Command> textWelcomeScreen = new HashMap<>();
  private Map<String, Command> textLoadScreen = new HashMap<>();
  private Map<String, Command> textBuildScreen = new HashMap<>();
  private Map<String, Command> textViewScreen = new HashMap<>();
  private Map<String, Command> textSaveScreen = new HashMap<>();

  private Map<String, Command> guiWelcomeScreen = new HashMap<>();
  private Map<String, Command> guiLoadScreen = new HashMap<>();
  private Map<String, Command> guiBuildScreen = new HashMap<>();
  private Map<String, Command> guiViewScreen = new HashMap<>();
  private Map<String, Command> guiSaveScreen = new HashMap<>();

  /**
   * The main method of the program which begins the controller and its functions.
   *
   * @param args main method stuff
   */
  public static void main(String[] args) {
    InputController in = new ControllerImpl(new ViewImpl(System.out),
        new PortfolioManagerImpl(new Persistence()),
        new InputStreamReader(System.in), System.out, new APIImpl());
    in.start();
  }


  /**
   * A constructor for the controller.
   *
   * @param view    the view
   * @param portMan the model
   * @param in      the input stream
   * @param out     the output stream
   * @param api     the class making API calls
   */
  public ControllerImpl(ViewInterface view, PortfolioManager portMan,
      Readable in, PrintStream out, API api) {
    this.v = view;
    this.p = portMan;
    this.sc = new Scanner(in);
    this.api = api;
  }

  /**
   * A method that starts the program and asks the user input and sends it for processing.
   */
  @Override
  public void start() {
    currentScreen = "WS";
    boolean uiSelect = true;
    while (uiSelect) {
      v.printLine("Please enter 'text' for a text-based interface. Alternatively, enter 'gui' "
          + "for a graphics interface.");
      String selection = sc.nextLine();
      if (selection.equals("gui") || selection.equals("text")) {
        ui = selection;
        uiSelect = false;
      }
    }

    setupMaps();

    if (ui.equals("text")) {
      v.showWelcomeScreen();
    } else {
      JFrameView.setDefaultLookAndFeelDecorated(false);
      f = new JFrameView(this);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setVisible(true);
      return;
    }

    Command com;
    Map<String, Map<String, Command>> menu;
    Map<String, Command> screen;
    String input;
    while (flag) {
      if (!flag) {
        break;
      }
      input = sc.nextLine();
      try {
        int inputInt = Integer.parseInt(input);
      } catch (Exception e) {
        v.printLine("Please be sure to enter an integer.");
        continue;
      }
      menu = uiMap.get(ui);
      screen = menu.get(currentScreen);
      com = screen.getOrDefault(input, null);

      if (com == null) {
        v.printLine("Please be sure to enter one of the available selections.");
      } else {
        ((TextCommand) com).goDoStuff(sc, v, p, api);
      }
    }
  }

  private void setupMaps() {
    uiMap.put("text", textMenus);
    uiMap.put("gui", guiMenus);

    textMenus.put("WS", textWelcomeScreen);
    textMenus.put("LS", textLoadScreen);
    textMenus.put("BS", textBuildScreen);
    textMenus.put("PS", textViewScreen);
    textMenus.put("SS", textSaveScreen);

    textWelcomeScreen.put("1", new LoadScreenCommand());
    textWelcomeScreen.put("2", new BuildScreenCommand());
    textWelcomeScreen.put("3", new ViewScreenCommand());
    textWelcomeScreen.put("4", new SaveScreenCommand());
    textWelcomeScreen.put("5", new ExitCommand());

    textLoadScreen.put("1", new LoadCommand());
    textLoadScreen.put("2", new BackCommand());

    textBuildScreen.put("1", new BuildSimpleCommand());
    textBuildScreen.put("2", new BuildFlexibleCommand());
    textBuildScreen.put("3", new StrategyBuildCommand());
    textBuildScreen.put("4", new EditFlexibleCommand());
    textBuildScreen.put("5", new StrategyCommand());
    textBuildScreen.put("6", new DollarCostBuyCommand());
    textBuildScreen.put("7", new BackCommand());

    textViewScreen.put("1", new ViewContentsCommand());
    textViewScreen.put("2", new PortfolioValueCommand());
    textViewScreen.put("3", new CostBasisCommand());
    textViewScreen.put("4", new PortfolioPerformanceCommand());
    textViewScreen.put("5", new ManualValuationCommand());
    textViewScreen.put("6", new BackCommand());

    textSaveScreen.put("1", new SaveCommand());
    textSaveScreen.put("2", new SaveAllCommand());
    textSaveScreen.put("3", new BackCommand());

    guiMenus.put("WS", guiWelcomeScreen);
    guiMenus.put("LS", guiLoadScreen);
    guiMenus.put("BS", guiBuildScreen);
    guiMenus.put("PS", guiViewScreen);
    guiMenus.put("SS", guiSaveScreen);

    guiWelcomeScreen.put("Load Button", new LoadScreenCommandGui());
    guiWelcomeScreen.put("Build Button", new BuildScreenCommandGui());
    guiWelcomeScreen.put("View Button", new ViewScreenCommandGui());
    guiWelcomeScreen.put("Save Button", new SaveScreenCommandGui());

    guiLoadScreen.put("Upload Port", new LoadCommandGui());
    guiLoadScreen.put("Back Button", new BackCommandGui());

    guiBuildScreen.put("Build/Edit Port", new BuildEditCommandGui());
    guiBuildScreen.put("Back Button", new BackCommandGui());
    guiBuildScreen.put("Set Portfolio Name", new BuildFlexibleCommandGui());
    guiBuildScreen.put("Add Stock", new EditFlexibleCommandGui());
    guiBuildScreen.put("Add Static Info for Strategy", new StrategyValidateInfoGuiCommand());
    guiBuildScreen.put("Add New Share to Strategy", new StrategyValidateStockGuiCommand());
    guiBuildScreen.put("Done build strategy", new StrategyGuiCommand());
    guiBuildScreen.put("Add Static Info for Dollar Cost", new DollarCostStartCommand());
    guiBuildScreen.put("Done Dollar Cost", new DollarCostBuyGuiCommand());

    guiViewScreen.put("View Port", new ViewCommandGui());
    guiViewScreen.put("Back Button", new BackCommandGui());
    guiViewScreen.put("Show Portfolio Contents", new ViewContentsGuiCommand());
    guiViewScreen.put("Show Portfolio Value", new PortfolioValueGuiCommand());
    guiViewScreen.put("Show Cost Basis", new CostBasisGuiCommand());

    guiSaveScreen.put("Save Port", new SaveCommandGui());
    guiSaveScreen.put("Save a specific portfolio", new SaveGuiCommand());
    guiSaveScreen.put("Back Button", new BackCommandGui());

  }

  /**
   * A method to listen to the gui for actions indicating the need for a new command.
   *
   * @param e the event to be processed
   */
  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    currentButton = action;
    comGo();
  }

  private void comGo() {
    Map<String, Map<String, Command>> menu = uiMap.get(ui);
    Map<String, Command> screen = menu.get(currentScreen);
    GuiCommand com = (GuiCommand) screen.getOrDefault(currentButton, null);

    if (com == null) {
      v.printLine("Please be sure to enter one of the available selections.");
    } else {
      com.goDoStuff(f, p, api);
    }
  }

  /**
   * A GuiCommand to take the user back to the starting screen.
   */
  class BackCommandGui implements GuiCommand {

    @Override
    public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
      currentScreen = "WS";
      f.showWelcomeScreen();
    }
  }

  /**
   * A GuiCommand to take the user to the load screen.
   */
  class LoadScreenCommandGui implements GuiCommand {

    @Override
    public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
      currentScreen = "LS";
      f.showLoadScreen();
    }
  }

  /**
   * A GuiCommand to take the user to the build/edit screen.
   */
  class BuildScreenCommandGui implements GuiCommand {

    @Override
    public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
      currentScreen = "BS";
      f.showBuildScreen();
    }
  }

  /**
   * A GuiCommand to take the user to the portfolio viewing screen.
   */
  class ViewScreenCommandGui implements GuiCommand {

    @Override
    public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
      currentScreen = "PS";
      f.showPortfolioScreen();
    }
  }

  /**
   * A GuiCommand to take the user to the save screen.
   */
  class SaveScreenCommandGui implements GuiCommand {

    @Override
    public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
      currentScreen = "SS";
      f.showSaveScreen();
    }
  }

  /**
   * A TextCommand to take the user back to the welcome screen.
   */
  class BackCommand implements TextCommand {

    @Override
    public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
      currentScreen = "WS";
      v.showWelcomeScreen();
    }
  }

  /**
   * A TextCommand to take the user to the load screen.
   */
  class LoadScreenCommand implements TextCommand {

    @Override
    public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
      currentScreen = "LS";
      v.showLoadScreen();
    }
  }

  /**
   * A TextCommand to take the user to the build/edit screen.
   */
  class BuildScreenCommand implements TextCommand {

    @Override
    public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
      currentScreen = "BS";
      v.showBuildScreen();
    }
  }

  /**
   * A TextCommand to take the user to the portfolio viewing screen.
   */
  class ViewScreenCommand implements TextCommand {

    @Override
    public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
      currentScreen = "PS";
      v.showPortfolioScreen();
    }
  }

  /**
   * A TextCommand to take the user to the save screen.
   */
  class SaveScreenCommand implements TextCommand {

    @Override
    public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
      currentScreen = "SS";
      v.showSaveScreen();
    }
  }

  /**
   * A TextCommand to exit the program.
   */
  class ExitCommand implements TextCommand {

    @Override
    public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
      flag = false;
    }
  }

}
