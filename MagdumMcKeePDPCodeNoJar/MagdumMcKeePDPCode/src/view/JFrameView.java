package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * An implementation of the GuiInterface which acts as a view in MVC.
 */
public class JFrameView extends JFrame implements GuiInterface {

  private JPanel contentPanel;
  private JPanel subContentPanel;
  private JLabel content;
  private final JButton loadButton;
  private final JButton buildButton;
  private final JButton viewButton;
  private final JButton saveButton;
  private final JButton backButton;
  private ActionListener actionListner;
  private Object[] opStuff = new Object[10];
  private String portfolioName = "";
  private Object[] conStuff = new Object[10];
  private String currScreen;
  private ArrayList<Object> tSList = new ArrayList<>();
  private int iter = 0;

  /**
   * A constructor for the JFrameView class.
   *
   * @param a the action listener receiving info about buttons pressed, etc.
   */
  public JFrameView(ActionListener a) {
    super();
    this.actionListner = a;
    setTitle("GROW MONEY INVESTMENT PLANNER");
    setSize(1000, 800);
    setLocation(100, 100);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    Border paddingMain = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    mainPanel.setBorder(paddingMain);
    add(mainPanel);

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, FlowLayout.LEFT));
    Border paddingHeader = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    headerPanel.setBorder(paddingHeader);
    mainPanel.add(headerPanel);
    JLabel header = new JLabel("Welcome to GROW MONEY!!!");
    header.setFont(new Font("Calibri", Font.BOLD, 20));
    headerPanel.add(header);

    JPanel subMainPanel = new JPanel();
    subMainPanel.setLayout(new GridLayout());
    Border paddingSubMain = BorderFactory.createEmptyBorder(20, 50, 20, 50);
    subMainPanel.setBorder(paddingSubMain);
    mainPanel.add(subMainPanel);

    JPanel menuPanel = new JPanel();
    menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
    Border paddingMenu = BorderFactory.createEmptyBorder(20, 0, 20, 0);
    menuPanel.setBorder(paddingMenu);
    subMainPanel.add(menuPanel);
    JLabel menu = new JLabel("MENU");
    Border padMenuLabel = BorderFactory.createEmptyBorder(0, 10, 20, 0);
    menu.setBorder(padMenuLabel);
    menu.setFont(new Font("Calibri", Font.BOLD, 15));
    menuPanel.add(menu);

    loadButton = new JButton("Load Portfolio");
    loadButton.setActionCommand("Load Button");
    loadButton.addActionListener(this.actionListner);
    menuPanel.add(loadButton);

    buildButton = new JButton("Build/Edit Portfolio");
    buildButton.setActionCommand("Build Button");
    buildButton.addActionListener(this.actionListner);
    menuPanel.add(buildButton);

    viewButton = new JButton("View Portfolio");
    viewButton.setActionCommand("View Button");
    viewButton.addActionListener(a);
    menuPanel.add(viewButton);

    saveButton = new JButton("Save Portfolio");
    saveButton.setActionCommand("Save Button");
    saveButton.addActionListener(this.actionListner);
    menuPanel.add(saveButton);

    backButton = new JButton("Back to Home");
    backButton.setActionCommand("Back Button");
    backButton.addActionListener(this.actionListner);
    menuPanel.add(backButton);
    backButton.setVisible(false);

    contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
    Border paddingContent = BorderFactory.createEmptyBorder(20, 0, 20, 20);
    contentPanel.setBorder(paddingContent);
    subMainPanel.add(contentPanel);

    JPanel contentHeaderPanel = new JPanel();
    contentHeaderPanel.setLayout(new BoxLayout(contentHeaderPanel, FlowLayout.LEFT));
    Border paddingConHeader = BorderFactory.createEmptyBorder(0, 0, 20, 50);
    contentHeaderPanel.setBorder(paddingConHeader);
    contentPanel.add(contentHeaderPanel);
    content = new JLabel("ABOUT US");
    content.setFont(new Font("Calibri", Font.BOLD, 18));
    contentHeaderPanel.add(content);

  }

  @Override
  public void showWelcomeScreen() {
    enableButtons();
    content.setText("ABOUT US");
    subContentPanel.setVisible(false);
  }

  @Override
  public void showLoadScreen() {
    disableButtons();
    content.setText("Load a Portfolio");

    addSubContentPanel();
    JLabel lblFile = new JLabel("Enter the filename : ");
    subContentPanel.add(lblFile);
    JTextField txtFile = new JTextField(15);
    txtFile.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtFile);

    JButton portButton = new JButton("Upload File");
    portButton.setActionCommand("Upload Port");
    portButton.addActionListener(evt -> {
      switch (currScreen) {
        case "Error":
          subContentPanel.setVisible(true);
          txtFile.setText("");
          break;
        case "Show Contents":
          subContentPanel.setVisible(false);
          String[] columnNames = {"Ticker", "Count", "Date"};
          Object[][] data = new Object[getConStuff().length / 3][3];
          int j = 0;
          for (int i = 0; i < getConStuff().length; i = i + 3) {
            data[j][0] = getConStuff()[i];
            data[j][1] = getConStuff()[i + 1];
            data[j][2] = getConStuff()[i + 2];
            j++;
          }
          showContents(data, columnNames);
          break;
        default:
          break;
      }
    });
    portButton.addActionListener(this.actionListner);
    portButton.addActionListener(evt -> {
      try {
        portfolioName = txtFile.getText();
        portfolioName = portfolioName.substring(0, portfolioName.length() - 4);
      } catch (Exception e) {
        //we do nothing
      }
      txtFile.setText("");
    });
    subContentPanel.add(portButton);
  }

  @Override
  public void showBuildScreen() {
    opStuff = new Object[1];
    disableButtons();
    content.setText("Build/Edit a Portfolio");

    addSubContentPanel();
    JLabel selectOption = new JLabel("Please select one of the options- ");
    subContentPanel.add(selectOption);
    String[] items = new String[6];
    items[0] = "Select Option";
    items[1] = "Begin building a flexible portfolio";
    items[2] = "Begin a flexible portfolio with a strategy";
    items[3] = "Edit a flexible portfolio";
    items[4] = "Add a strategy to a flexible portfolio";
    items[5] = "Add a fixed cost buy across a flexible portfolio";
    JComboBox<String> options = new JComboBox<>(items);
    subContentPanel.add(options);

    JButton submit = new JButton("Submit");
    submit.setActionCommand("Build/Edit Port");
    submit.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      switch (currScreen) {
        case "Build Portfolio":
        case "Build Strategy":
          setFlexNameScreen();
          break;
        case "Edit Portfolio":
          editFlexPortScreen();
          break;
        case "Edit Strategy":
          editStrategyScreen();
          break;
        case "Dollar Cost":
          fixedCostBuyScreen();
          break;
        case "Error":
          subContentPanel.setVisible(true);
          break;
        default:
          subContentPanel.setVisible(true);
          break;
      }
    });
    submit.addActionListener(this.actionListner);
    submit.addActionListener(evt -> opStuff[0] = options.getSelectedItem());
    subContentPanel.add(submit);
  }

  //setting portfolio name while building portfolio/strategy
  private void setFlexNameScreen() {
    content.setText("Build Portfolio");
    String tempCurrScreen = currScreen;
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Enter portfolio name : ");
    subContentPanel.add(lblPortName);
    JTextField txtPortName = new JTextField(15);
    txtPortName.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtPortName);

    JButton portName = new JButton("Set Portfolio Name");
    portName.setActionCommand("Set Portfolio Name");
    portName.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      switch (currScreen) {
        case "Error":
          subContentPanel.setVisible(true);
          txtPortName.setText("");
          currScreen = tempCurrScreen;
          break;
        case "Add Stock":
          addStocks();
          break;
        case "Add Strategy":
          addStrategy();
          break;
        default:
          break;
      }
    });
    portName.addActionListener(this.actionListner);
    portName.addActionListener(evt -> portfolioName = txtPortName.getText());
    subContentPanel.add(portName);
  }

  //editing flexible portfolio
  private void editFlexPortScreen() {
    content.setText("Edit Portfolio");
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio to edit : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JButton portButton = new JButton("Get Portfolio");
    portButton.setActionCommand("Get Portfolio Name");
    portButton.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      addStocks();
    });
    portButton.addActionListener(evt -> portfolioName = portNames.getSelectedItem().toString());
    subContentPanel.add(portButton);
  }

  //editing strategy
  private void editStrategyScreen() {
    content.setText("Edit Strategy");
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio to edit strategy : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JButton portButton = new JButton("Get Portfolio");
    portButton.setActionCommand("Get Portfolio Name");
    portButton.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      currScreen = "Edit Strategy";
      addStrategy();
    });
    portButton.addActionListener(evt -> portfolioName = portNames.getSelectedItem().toString());
    subContentPanel.add(portButton);
  }

  //adding Stocks for flexible portfolio
  private void addStocks() {
    content.setText("Build Portfolio");
    opStuff = new Object[6];
    addSubContentPanel();
    JLabel lblBuySell = new JLabel("Buy Or Sell this stock? :");
    subContentPanel.add(lblBuySell);
    String[] items = new String[2];
    items[0] = "Buy Stock";
    items[1] = "Sell Stock";
    JComboBox<String> options = new JComboBox<>(items);
    subContentPanel.add(options);

    JLabel lblTicker = new JLabel("Enter the Ticker : ");
    subContentPanel.add(lblTicker);
    JTextField txtTicker = new JTextField(15);
    txtTicker.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtTicker);

    JLabel lblStocks = new JLabel("Enter the Stock Count : ");
    subContentPanel.add(lblStocks);
    JTextField txtStocks = new JTextField(15);
    txtStocks.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtStocks);

    JLabel lblDate = new JLabel("Enter the Date (YYYY-MM-DD) : ");
    subContentPanel.add(lblDate);
    JLabel lblSlash = new JLabel("/");
    JTextField txtYear = new JTextField(4);
    txtStocks.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtYear);
    JTextField txtMon = new JTextField(2);
    txtMon.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtMon);
    JTextField txtDay = new JTextField(2);
    txtDay.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtDay);

    subContentPanel.add(new JLabel("Please click on 'Back to Home' to quit, "));
    subContentPanel.add(new JLabel("else 'Add Stocks' to enter Stock Info."));
    JButton addStock = new JButton("Add Stock");
    addStock.setActionCommand("Add Stock");
    addStock.addActionListener(evt -> opStuff = new Object[6]);
    addStock.addActionListener(this.actionListner);
    addStock.addActionListener(evt -> {
      opStuff[0] = options.getSelectedItem();
      opStuff[1] = txtTicker.getText();
      opStuff[2] = txtStocks.getText();
      opStuff[3] = txtYear.getText();
      opStuff[4] = txtMon.getText();
      opStuff[5] = txtDay.getText();
      txtTicker.setText("");
      txtStocks.setText("");
      txtYear.setText("");
      txtMon.setText("");
      txtDay.setText("");
    });
    subContentPanel.add(addStock);

  }

  //adding Strategy for flexible portfolio
  private void addStrategy() {
    if (currScreen.equals("Edit Strategy")) {
      content.setText("Edit Portfolio with Strategy");
    } else {
      content.setText("Build Portfolio with Strategy");
    }
    opStuff = new Object[8];
    addSubContentPanel();

    JLabel lblAmount = new JLabel("Enter Dollar Amount : ");
    subContentPanel.add(lblAmount);
    JTextField txtAmount = new JTextField(10);
    txtAmount.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtAmount);

    JLabel lblFreq = new JLabel("No. of days between buys : ");
    subContentPanel.add(lblFreq);
    JTextField txtFreq = new JTextField(10);
    txtFreq.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtFreq);

    JLabel lblStartDate = new JLabel("Start Date (YYYY-MM-DD) : ");
    subContentPanel.add(lblStartDate);
    JTextField txtStartYear = new JTextField(4);
    txtStartYear.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtStartYear);
    JTextField txtStartMon = new JTextField(2);
    txtStartMon.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtStartMon);
    JTextField txtStartDay = new JTextField(2);
    txtStartDay.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtStartDay);

    JLabel lblEndDate = new JLabel("End Date (YYYY-MM-DD) : ");
    subContentPanel.add(lblEndDate);
    JTextField txtEndYear = new JTextField(4);
    txtEndYear.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtEndYear);
    JTextField txtEndMon = new JTextField(2);
    txtEndMon.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtEndMon);
    JTextField txtEndDay = new JTextField(2);
    txtEndDay.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtEndDay);

    JLabel lblMsg = new JLabel("Please leave the End Date blank, if the strategy is on-going.");
    lblMsg.setFont(new Font("Calibri", Font.BOLD, 12));
    subContentPanel.add(lblMsg);
    JButton proceed = new JButton("Proceed");
    proceed.setActionCommand("Add Static Info for Strategy");
    proceed.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      switch (currScreen) {
        case "Error":
          subContentPanel.setVisible(true);
          break;
        case "Proceed Build":
        case "Proceed Edit":
          addTickerCountFrame(opStuff, currScreen);
          break;
        default:
          break;
      }
    });
    proceed.addActionListener(this.actionListner);
    proceed.addActionListener(evt -> {
      opStuff[0] = txtAmount.getText();
      opStuff[1] = txtFreq.getText();
      opStuff[2] = txtStartYear.getText();
      opStuff[3] = txtStartMon.getText();
      opStuff[4] = txtStartDay.getText();
      opStuff[5] = txtEndYear.getText();
      opStuff[6] = txtEndMon.getText();
      opStuff[7] = txtEndDay.getText();
      txtAmount.setText("");
      txtFreq.setText("");
      txtStartYear.setText("");
      txtStartMon.setText("");
      txtStartDay.setText("");
      txtEndYear.setText("");
      txtEndMon.setText("");
      txtEndDay.setText("");
    });
    subContentPanel.add(proceed);
  }

  //add scrollable entry screen for adding tickers and shares in Strategy
  private void addTickerCountFrame(Object[] op, String cS) {
    String tempCurrScreen = cS;
    addSubContentPanel();
    tSList = new ArrayList<>();
    JPanel tickerCountFrame = new JPanel();
    tickerCountFrame.setLayout(new BoxLayout(tickerCountFrame, BoxLayout.Y_AXIS));
    Border paddingTCFrame = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    tickerCountFrame.setBorder(paddingTCFrame);
    JScrollPane tickerCountScroll = new JScrollPane(tickerCountFrame);
    tickerCountScroll.setHorizontalScrollBar(null);
    tickerCountScroll.setPreferredSize(new Dimension(400, 250));
    subContentPanel.add(tickerCountScroll);

    JPanel displayContentPanel = new JPanel();
    displayContentPanel.setLayout(new BoxLayout(displayContentPanel, BoxLayout.Y_AXIS));
    Border paddingDisplay = BorderFactory.createEmptyBorder(20, 0, 0, 0);
    displayContentPanel.setBorder(paddingDisplay);
    JScrollPane displayScroll = new JScrollPane(displayContentPanel);
    displayScroll.setHorizontalScrollBar(null);
    displayScroll.setPreferredSize(new Dimension(400, 250));
    subContentPanel.add(displayScroll);
    JPanel displayHeader = new JPanel();
    displayHeader.setLayout(new BoxLayout(displayHeader, FlowLayout.LEFT));
    Border paddingDisHeader = BorderFactory.createEmptyBorder(0, 0, 20, 50);
    displayHeader.setBorder(paddingDisHeader);
    displayContentPanel.add(displayHeader);
    JLabel disContent = new JLabel("CONTENTS-");
    disContent.setFont(new Font("Calibri", Font.BOLD, 16));
    displayHeader.add(disContent);

    JButton doneBuild = new JButton("Update Strategy");

    if (tempCurrScreen.equals("Proceed Build")) {
      addTickerCount(op, tickerCountFrame, displayContentPanel);
      doneBuild.setActionCommand("Done build strategy");
    }
    if (tempCurrScreen.equals("Proceed Edit")) {
      iter = 0;
      addCount(op, tickerCountFrame, displayContentPanel);
      doneBuild.setActionCommand("Done build strategy");
    }
    if (tempCurrScreen.equals("Proceed Dollar Cost")) {
      //doneBuild.setVisible(false);
      iter = 0;
      addDCCount(op, tickerCountFrame, displayContentPanel);
      doneBuild.setActionCommand("Done Dollar Cost");
    }

    doneBuild.addActionListener(evt -> {
      tSList = new ArrayList<>();
      switch (currScreen) {
        case "Error":
          subContentPanel.setVisible(false);
          addTickerCountFrame(op, tempCurrScreen);
          break;
        case "Show Contents":
          subContentPanel.setVisible(false);
          subContentPanel.setVisible(false);
          String[] columnNames = {"Ticker", "Share Count", "Date"};
          Object[][] data = new Object[getConStuff().length / 3][3];
          int j = 0;
          for (int i = 0; i < getConStuff().length; i = i + 3) {
            data[j][0] = getConStuff()[i];
            data[j][1] = getConStuff()[i + 1];
            data[j][2] = getConStuff()[i + 2];
            j++;
          }
          showContents(data, columnNames);
          break;
        default:
          break;
      }
    });
    doneBuild.addActionListener(this.actionListner);
    doneBuild.addActionListener(evt -> {
      Object[] staticInfo = op;
      int opStuffLen = op.length + tSList.size();
      opStuff = new Object[opStuffLen];
      for (int i = 0; i < op.length; i++) {
        opStuff[i] = op[i];
      }
      for (int i = 0; i < tSList.size(); i++) {
        opStuff[i + op.length] = tSList.get(i);
        System.out.println(opStuff[i + op.length]);
      }
    });
    subContentPanel.add(doneBuild);
  }

  //add tickers and shares continuously in Strategy while building
  private void addTickerCount(Object[] op, JPanel tickerCountFrame,
      JPanel displayContentPanel) {
    Object[] staticInfo = op;

    JPanel subTCFrame = new JPanel();
    subTCFrame.setLayout(new FlowLayout());
    Border paddingSubTCFrame = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    subTCFrame.setBorder(paddingSubTCFrame);
    tickerCountFrame.add(subTCFrame);

    JPanel subDisplay = new JPanel();
    subDisplay.setLayout(new FlowLayout());
    Border paddingSubDisplay = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    subDisplay.setBorder(paddingSubDisplay);
    displayContentPanel.add(subDisplay);
    subDisplay.setVisible(false);

    JLabel lblTicker = new JLabel("Ticker : ");
    subTCFrame.add(lblTicker);
    JTextField txtTicker = new JTextField(7);
    txtTicker.setFont(new Font("Calibri", Font.PLAIN, 12));
    subTCFrame.add(txtTicker);

    JLabel lblShare = new JLabel("Share : ");
    subTCFrame.add(lblShare);
    JTextField txtShare = new JTextField(7);
    txtShare.setFont(new Font("Calibri", Font.PLAIN, 12));
    subTCFrame.add(txtShare);

    JButton addShare = new JButton("Add Share");
    addShare.setActionCommand("Add New Share to Strategy");
    addShare.addActionListener(evt -> {
      switch (currScreen) {
        case "Error":
          txtShare.setText("");
          break;
        case "Validated":
          subDisplay.add(new JLabel("Ticker : " + txtTicker.getText()
              + ", Share : " + txtShare.getText()));
          tSList.add(txtTicker.getText());
          tSList.add(txtShare.getText());
          subDisplay.setVisible(true);
          addShare.setVisible(false);
          txtTicker.setEditable(false);
          txtShare.setEditable(false);
          addTickerCount(staticInfo, tickerCountFrame, displayContentPanel);
          break;
        default:
          break;
      }
    });
    addShare.addActionListener(this.actionListner);
    addShare.addActionListener(evt -> {
      int sLen = staticInfo.length;
      opStuff = new Object[sLen + 2];
      for (int i = 0; i < sLen; i++) {
        opStuff[i] = staticInfo[i];
      }
      opStuff[sLen] = txtTicker.getText();
      opStuff[sLen + 1] = txtShare.getText();
    });
    subTCFrame.add(addShare);

  }

  //add shares continuously in Strategy while editing
  private void addCount(Object[] op, JPanel tickerCountFrame,
      JPanel displayContentPanel) {
    Object[] staticInfo = op;

    JPanel subTCFrame = new JPanel();
    subTCFrame.setLayout(new FlowLayout());
    Border paddingSubTCFrame = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    subTCFrame.setBorder(paddingSubTCFrame);
    tickerCountFrame.add(subTCFrame);

    JPanel subDisplay = new JPanel();
    subDisplay.setLayout(new FlowLayout());
    Border paddingSubDisplay = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    subDisplay.setBorder(paddingSubDisplay);
    displayContentPanel.add(subDisplay);
    subDisplay.setVisible(false);

    JLabel lblTicker = new JLabel("Ticker : ");
    subTCFrame.add(lblTicker);
    JTextField txtTicker = new JTextField(7);
    txtTicker.setFont(new Font("Calibri", Font.PLAIN, 12));
    txtTicker.setText(conStuff[iter].toString());
    txtTicker.setEditable(false);
    subTCFrame.add(txtTicker);

    JLabel lblShare = new JLabel("Share : ");
    subTCFrame.add(lblShare);
    JTextField txtShare = new JTextField(7);
    txtShare.setFont(new Font("Calibri", Font.PLAIN, 12));
    subTCFrame.add(txtShare);

    JButton addShare = new JButton("Add Share");
    addShare.setActionCommand("Add New Share to Strategy");
    addShare.addActionListener(evt -> {
      switch (currScreen) {
        case "Error":
          txtShare.setText("");
          break;
        case "Validated":
          subDisplay.add(new JLabel("Ticker : " + txtTicker.getText()
              + ", Share : " + txtShare.getText()));
          tSList.add(txtTicker.getText());
          tSList.add(txtShare.getText());
          subDisplay.setVisible(true);
          addShare.setVisible(false);
          txtTicker.setEditable(false);
          txtShare.setEditable(false);
          iter++;
          if (iter < conStuff.length) {
            addCount(staticInfo, tickerCountFrame, displayContentPanel);
          }
          break;
        default:
          break;
      }
    });
    addShare.addActionListener(this.actionListner);
    addShare.addActionListener(evt -> {
      int sLen = staticInfo.length;
      opStuff = new Object[sLen + 2];
      for (int i = 0; i < sLen; i++) {
        opStuff[i] = staticInfo[i];
      }
      opStuff[sLen] = txtTicker.getText();
      opStuff[sLen + 1] = txtShare.getText();
    });

    subTCFrame.add(addShare);
  }

  //calculating fixed cost buy for a flexible portfolio
  private void fixedCostBuyScreen() {
    content.setText("Dollar Cost Average-Buy");
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JButton portButton = new JButton("Get Portfolio");
    portButton.setActionCommand("Get Portfolio Name");
    portButton.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      //currScreen = "Dollar Cost Buy";
      addDollarCost();
    });
    portButton.addActionListener(evt -> portfolioName = portNames.getSelectedItem().toString());
    subContentPanel.add(portButton);
  }

  private void addDollarCost() {
    opStuff = new Object[4];
    addSubContentPanel();

    JLabel lblAmount = new JLabel("Enter Dollar Amount : ");
    subContentPanel.add(lblAmount);
    JTextField txtAmount = new JTextField(10);
    txtAmount.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtAmount);

    JLabel lblDate = new JLabel("Enter a Date (YYYY-MM-DD) : ");
    subContentPanel.add(lblDate);
    JTextField txtYear = new JTextField(4);
    txtYear.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtYear);
    JTextField txtMon = new JTextField(2);
    txtMon.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtMon);
    JTextField txtDay = new JTextField(2);
    txtDay.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtDay);

    JButton proceed = new JButton("Proceed");
    proceed.setActionCommand("Add Static Info for Dollar Cost");
    proceed.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      switch (currScreen) {
        case "Error":
          subContentPanel.setVisible(true);
          break;
        case "Proceed Dollar Cost":
          addTickerCountFrame(opStuff, currScreen);
          break;
        default:
          break;
      }
    });
    proceed.addActionListener(this.actionListner);
    proceed.addActionListener(evt -> {
      opStuff[0] = txtAmount.getText();
      opStuff[1] = txtYear.getText();
      opStuff[2] = txtMon.getText();
      opStuff[3] = txtDay.getText();
      txtAmount.setText("");
      txtYear.setText("");
      txtMon.setText("");
      txtDay.setText("");
    });
    subContentPanel.add(proceed);
  }

  //add shares continuously in Dollar-Cost Averaging
  private void addDCCount(Object[] op, JPanel tickerCountFrame,
      JPanel displayContentPanel) {
    Object[] staticInfo = op;

    JPanel subTCFrame = new JPanel();
    subTCFrame.setLayout(new FlowLayout());
    Border paddingSubTCFrame = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    subTCFrame.setBorder(paddingSubTCFrame);
    tickerCountFrame.add(subTCFrame);

    JPanel subDisplay = new JPanel();
    subDisplay.setLayout(new FlowLayout());
    Border paddingSubDisplay = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    subDisplay.setBorder(paddingSubDisplay);
    displayContentPanel.add(subDisplay);
    subDisplay.setVisible(false);

    JLabel lblTicker = new JLabel("Ticker : ");
    subTCFrame.add(lblTicker);
    JTextField txtTicker = new JTextField(7);
    txtTicker.setFont(new Font("Calibri", Font.PLAIN, 12));
    txtTicker.setText(conStuff[iter].toString());
    txtTicker.setEditable(false);
    subTCFrame.add(txtTicker);

    JLabel lblShare = new JLabel("Share : ");
    subTCFrame.add(lblShare);
    JTextField txtShare = new JTextField(7);
    txtShare.setFont(new Font("Calibri", Font.PLAIN, 12));
    subTCFrame.add(txtShare);

    JButton addDCShare = new JButton("Add Share");
    addDCShare.setActionCommand("Add New Share to Dollar Cost");
    addDCShare.addActionListener(evt -> {
      subDisplay.add(new JLabel("Ticker : " + txtTicker.getText()
          + ", Share : " + txtShare.getText()));
      tSList.add(txtTicker.getText());
      tSList.add(txtShare.getText());
      subDisplay.setVisible(true);
      addDCShare.setVisible(false);
      txtTicker.setEditable(false);
      txtShare.setEditable(false);
      iter++;
      if (iter < conStuff.length) {
        addDCCount(staticInfo, tickerCountFrame, displayContentPanel);
      }
    });
    addDCShare.addActionListener(this.actionListner);
    addDCShare.addActionListener(evt -> {
      int sLen = staticInfo.length;
      opStuff = new Object[sLen + 2];
      for (int i = 0; i < sLen; i++) {
        opStuff[i] = staticInfo[i];
      }
      opStuff[sLen] = txtTicker.getText();
      opStuff[sLen + 1] = txtShare.getText();
    });

    subTCFrame.add(addDCShare);
  }

  @Override
  public void showPortfolioScreen() {
    opStuff = new Object[1];
    disableButtons();
    content.setText("View a Portfolio");

    addSubContentPanel();
    JLabel selectOption = new JLabel("Please select one of the options- ");
    subContentPanel.add(selectOption);
    String[] items = new String[4];
    items[0] = "Select Option";
    items[1] = "View contents of a portfolio";
    items[2] = "View value of a portfolio on a certain date";
    items[3] = "View cost basis of a portfolio on a certain date";
    JComboBox<String> options = new JComboBox<>(items);
    subContentPanel.add(options);

    JButton submit = new JButton("Submit");
    submit.setActionCommand("View Port");
    submit.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      switch (currScreen) {
        case "Show Contents":
          showPortContentScreen();
          break;
        case "Show Value":
          showPortValueScreen();
          break;
        case "Show Cost Basis":
          showCostBasisScreen();
          break;
        case "Error":
          subContentPanel.setVisible(true);
          break;
        default:
          subContentPanel.setVisible(true);
          break;
      }
    });
    submit.addActionListener(this.actionListner);
    submit.addActionListener(evt -> opStuff[0] = options.getSelectedItem());
    subContentPanel.add(submit);
  }

  private void showPortContentScreen() {
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JButton portButton = new JButton("Get Portfolio");
    portButton.setActionCommand("Show Portfolio Contents");
    portButton.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      String[] columnNames = {"Ticker", "Count", "Date"};
      Object[][] data = new Object[getConStuff().length / 3][3];
      int j = 0;
      for (int i = 0; i < getConStuff().length; i = i + 3) {
        data[j][0] = getConStuff()[i];
        data[j][1] = getConStuff()[i + 1];
        data[j][2] = getConStuff()[i + 2];
        j++;
      }
      showContents(data, columnNames);
    });
    portButton.addActionListener(this.actionListner);
    portButton.addActionListener(evt -> portfolioName = portNames.getSelectedItem().toString());
    subContentPanel.add(portButton);
  }

  private void showPortValueScreen() {
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JLabel lblDate = new JLabel("Enter Date (YYYY-MM-DD) : ");
    subContentPanel.add(lblDate);
    JTextField txtYear = new JTextField(4);
    txtYear.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtYear);
    JTextField txtMon = new JTextField(2);
    txtMon.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtMon);
    JTextField txtDay = new JTextField(2);
    txtDay.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtDay);

    JButton portButton = new JButton("Get Portfolio Value");
    portButton.setActionCommand("Show Portfolio Value");
    portButton.addActionListener(evt -> {
      if (currScreen.equals("Error")) {
        subContentPanel.setVisible(true);
      }
      else if (currScreen.equals("Value Extracted")) {
        subContentPanel.setVisible(false);
        Object[] out = new Object[getConStuff().length - 2];
        int j = 1;
        for (int i = 0; i < out.length; i++) {
          out[i] = getConStuff()[j];
          j++;
        }
        String[] columnNames = {"Ticker", "Count", "Date", "Value"};
        Object[][] data = new Object[out.length / 4][4];
        j = 0;
        for (int i = 0; i < out.length; i = i + 4) {
          data[j][0] = out[i];
          data[j][1] = out[i + 1];
          data[j][2] = out[i + 2];
          data[j][3] = out[i + 3];
          j++;
        }
        showContents(data, columnNames);
        subContentPanel.add(new JLabel("          Total Value of Portfolio on "
                + getConStuff()[0] + " is : " + getConStuff()[getConStuff().length - 1] + "$"));
      }
    });
    portButton.addActionListener(this.actionListner);
    portButton.addActionListener(evt -> {
      portfolioName = portNames.getSelectedItem().toString();
      opStuff = new Object[3];
      opStuff[0] = txtYear.getText();
      opStuff[1] = txtMon.getText();
      opStuff[2] = txtDay.getText();
      txtYear.setText("");
      txtMon.setText("");
      txtDay.setText("");
    });
    subContentPanel.add(portButton);
  }

  private void showCostBasisScreen() {
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JLabel lblDate = new JLabel("Enter Date (YYYY-MM-DD) : ");
    subContentPanel.add(lblDate);
    JTextField txtYear = new JTextField(4);
    txtYear.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtYear);
    JTextField txtMon = new JTextField(2);
    txtMon.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtMon);
    JTextField txtDay = new JTextField(2);
    txtDay.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtDay);

    JLabel lblCommission = new JLabel("Enter a Commission Fee (ex. xx.yy $) : ");
    subContentPanel.add(lblCommission);
    JTextField txtCommission = new JTextField(4);
    txtCommission.setFont(new Font("Calibri", Font.PLAIN, 12));
    subContentPanel.add(txtCommission);

    JButton portButton = new JButton("Get Cost Basis");
    portButton.setActionCommand("Show Cost Basis");
    portButton.addActionListener(evt -> {
      if (currScreen.equals("Error")) {
        subContentPanel.setVisible(true);
      }
      else if (currScreen.equals("Cost Basis")) {
        subContentPanel.setVisible(false);
        Object[] out = new Object[getConStuff().length - 3];
        int j = 1;
        for (int i = 0; i < out.length; i++) {
          out[i] = getConStuff()[j];
          j++;
        }
        String[] columnNames = {"Ticker", "Count", "Date", "Cost"};
        Object[][] data = new Object[out.length / 4][4];
        j = 0;
        for (int i = 0; i < out.length; i = i + 4) {
          data[j][0] = out[i];
          data[j][1] = out[i + 1];
          data[j][2] = out[i + 2];
          data[j][3] = out[i + 3];
          j++;
        }
        showContents(data, columnNames);
        subContentPanel.add(new JLabel("        Total Cost Basis of Portfolio on "
                + getConStuff()[0] + " is : " + getConStuff()[getConStuff().length - 1] + "$"));
        subContentPanel.add(new JLabel("        Total Amount Spent on Commission Fee is "
                + getConStuff()[getConStuff().length - 2] + "$"));
      }
    });
    portButton.addActionListener(this.actionListner);
    portButton.addActionListener(evt -> {
      portfolioName = portNames.getSelectedItem().toString();
      opStuff = new Object[4];
      opStuff[0] = txtYear.getText();
      opStuff[1] = txtMon.getText();
      opStuff[2] = txtDay.getText();
      opStuff[3] = txtCommission.getText();
      txtYear.setText("");
      txtMon.setText("");
      txtDay.setText("");
      txtCommission.setText("");
    });
    subContentPanel.add(portButton);


  }

  @Override
  public void showSaveScreen() {
    opStuff = new Object[1];
    disableButtons();
    content.setText("Save a Portfolio");

    addSubContentPanel();
    JLabel selectOption = new JLabel("Please select one of the options- ");
    subContentPanel.add(selectOption);
    String[] items = new String[3];
    items[0] = "Select Option";
    items[1] = "Save a specific portfolio";
    items[2] = "Save all portfolios";
    JComboBox<String> options = new JComboBox<>(items);
    subContentPanel.add(options);

    JButton submit = new JButton("Submit");
    submit.setActionCommand("Save Port");
    submit.addActionListener(evt -> {
      subContentPanel.setVisible(false);
      switch (currScreen) {
        case "Save Portfolio":
          savePortfolio();
          break;
        case "Save All Portfolios":
        case "Error":
          subContentPanel.setVisible(true);
          break;
        default:
          break;
      }
    });
    submit.addActionListener(this.actionListner);
    submit.addActionListener(evt -> opStuff[0] = options.getSelectedItem());
    subContentPanel.add(submit);
  }

  private void savePortfolio() {
    addSubContentPanel();

    JLabel lblPortName = new JLabel("Select a portfolio : ");
    subContentPanel.add(lblPortName);
    Object[] o = getConStuff();
    String[] item = new String[o.length];
    for (int i = 0; i < o.length; i++) {
      item[i] = o[i].toString();
    }
    JComboBox<String> portNames = new JComboBox<>(item);
    subContentPanel.add(portNames);

    JButton portButton = new JButton("Save Portfolio");
    portButton.setActionCommand("Save a specific portfolio");
    portButton.addActionListener(this.actionListner);
    subContentPanel.add(portButton);
  }

  //display validation errors given by the controller
  @Override
  public void printLine(String line) {
    JOptionPane.showMessageDialog(subContentPanel, line);
  }

  @Override
  public void printLines(String[] lines) {
    for (int i = 0; i < lines.length; i++) {
      JOptionPane.showMessageDialog(subContentPanel, lines);
    }
  }

  //display warning for invalid tickers
  @Override
  public String printWarning(String line) {
    int a = JOptionPane.showConfirmDialog(subContentPanel, line);
    if (a == JOptionPane.YES_OPTION) {
      return "y";
    } else {
      return "n";
    }
  }

  //disable menu buttons
  private void disableButtons() {
    loadButton.setVisible(false);
    buildButton.setVisible(false);
    viewButton.setVisible(false);
    saveButton.setVisible(false);
    backButton.setVisible(true);
  }

  //enable menu buttons
  private void enableButtons() {
    loadButton.setVisible(true);
    buildButton.setVisible(true);
    viewButton.setVisible(true);
    saveButton.setVisible(true);
    backButton.setVisible(false);
  }

  //add a panel for displaying all the contents on which user in operating
  private void addSubContentPanel() {
    subContentPanel = new JPanel();
    subContentPanel.setLayout(new FlowLayout(10, 5, 5));
    Border paddingSubCon = BorderFactory.createEmptyBorder(0, 0, 10, 50);
    subContentPanel.setBorder(paddingSubCon);
    contentPanel.add(subContentPanel);
  }

  //display contents of the portfolio
  private void showContents(Object[][] data, String[] columnNames) {
    addSubContentPanel();
    JPanel displayContentPanel = new JPanel();
    displayContentPanel.setLayout(new BoxLayout(displayContentPanel, BoxLayout.Y_AXIS));
    Border paddingDisplay = BorderFactory.createEmptyBorder(20, 0, 0, 0);
    displayContentPanel.setBorder(paddingDisplay);
    subContentPanel.add(displayContentPanel);
    JPanel displayHeader = new JPanel();
    displayHeader.setLayout(new BoxLayout(displayHeader, FlowLayout.LEFT));
    Border paddingDisHeader = BorderFactory.createEmptyBorder(0, 0, 20, 50);
    displayHeader.setBorder(paddingDisHeader);
    displayContentPanel.add(displayHeader);
    JLabel disContent = new JLabel("Portfolio Contents-");
    disContent.setFont(new Font("Calibri", Font.BOLD, 14));
    displayHeader.add(disContent);

    JPanel subDisplay = new JPanel();
    subDisplay.setLayout(new FlowLayout());
    Border paddingSubDisplay = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    subDisplay.setBorder(paddingSubDisplay);
    displayContentPanel.add(subDisplay);

    JTable contentTable = new JTable(data, columnNames);
    JScrollPane subDisplayScroll = new JScrollPane(contentTable);
    contentTable.setFillsViewportHeight(true);
    contentTable.getColumnModel().getColumn(0).setPreferredWidth(5);
    contentTable.getColumnModel().getColumn(1).setPreferredWidth(7);
    contentTable.getColumnModel().getColumn(2).setPreferredWidth(7);
    contentTable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 13));
    subDisplay.add(subDisplayScroll);

  }

  //get operational data(ex. tickers, stocks, dates) to send to controller
  @Override
  public Object[] getOperationalStuff() {
    return opStuff;
  }

  //get portfolio name to send to controller
  @Override
  public String getPortfolioName() {
    return portfolioName;
  }

  //controller uses this method to set data generated by it
  @Override
  public void setConStuff(Object[] o) {
    conStuff = o;
  }

  //get the data set by the controller
  private Object[] getConStuff() {
    return conStuff;
  }

  //controller sets the screen
  @Override
  public void setCurrScreen(String str) {
    currScreen = str;
  }

  //get the current screen to perform corresponding
  @Override
  public String getCurrScreen() {
    return currScreen;
  }
}
