package controller;

import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * An interface defining the commands for the text ui, for use by the controller.
 */
public interface TextCommand extends Command {

  /**
   * The primary method defining the function of the command.
   *
   * @param sc the input reader
   * @param v the view interface speaking with the controller
   * @param p the model speaking with the controller
   * @param api the class handling calls to an API
   */
  void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api);
}
