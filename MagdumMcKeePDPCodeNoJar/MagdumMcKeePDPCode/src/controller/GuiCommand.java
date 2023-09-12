package controller;

import model.PortfolioManager;
import view.GuiInterface;

/**
 * An interface defining the characteristics of a GuiCommand, for use by the controller.
 */
public interface GuiCommand extends Command {

  /**
   * The primary method which defines the actions taken by the given command.
   *
   * @param f the gui interface talking to the controller
   * @param p the model talking to the controller
   * @param api the class the handles making calls to an API
   */
  void goDoStuff(GuiInterface f, PortfolioManager p, API api);
}
