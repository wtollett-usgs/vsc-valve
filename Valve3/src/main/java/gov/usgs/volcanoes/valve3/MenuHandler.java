package gov.usgs.volcanoes.valve3;

import gov.usgs.volcanoes.core.configfile.ConfigFile;
import gov.usgs.volcanoes.core.util.StringUtils;
import gov.usgs.volcanoes.valve3.data.DataHandler;
import gov.usgs.volcanoes.valve3.data.DataSourceDescriptor;
import gov.usgs.volcanoes.valve3.result.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates menu from http request. Keeps information about sections and menu items,
 * parse configuration to initialize them.
 *
 * @author Dan Cervelli
 */
public class MenuHandler implements HttpHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuHandler.class);

  /**
   * map of section name-section pairs.
   */
  private Map<String, Section> sections;
  /**
   * map of data source descriptor name-menu item pairs.
   */
  private Map<String, MenuItem> items;

  /**
   * Constructor.
   *
   * @param dh data handler for this menu handler
   */
  public MenuHandler(DataHandler dh) {
    sections = new HashMap<String, Section>();
    items = new HashMap<String, MenuItem>();

    ConfigFile config = dh.getConfig();
    List<String> ss = config.getList("section");
    for (String sec : ss) {
      boolean expanded = false;
      try {
        expanded = config.getBoolean(sec + ".expanded");
      } catch (Exception e) {
        LOGGER.debug("No expanded key for section: {}", sec);
      }
      Section section = new Section(sec, "",
                                    Integer.parseInt(config.getString(sec + ".sortOrder")),
                                    expanded);
      sections.put(sec, section);
    }

    List<DataSourceDescriptor> sources = dh.getDataSources();
    for (DataSourceDescriptor dsd : sources) {
      ConfigFile cf = dsd.getConfig();
      String sec = cf.getString("section");
      if (sec != null) {
        Section section = sections.get(sec);
        if (section != null) {
          char lineType;
          String value = cf.getString("plotter.lineType");
          if (value == null) {
            lineType = 'l';
          } else {
            if (value.length() != 1) {
              lineType = 'l';
            } else {
              lineType = value.charAt(0);
            }
          }
          char biasType;
          value = cf.getString("biasType");
          if (value == null) {
            biasType = '0';
          } else if (value.equals("mean")) {
            biasType = '1';
          } else if (value.equals("initial")) {
            biasType = '2';
          } else {
            biasType = '0';
          }

          boolean plotSeparately = StringUtils.stringToBoolean(
                                     cf.getString("plotter.plotSeparately"), false);
          boolean barDisplay = StringUtils.stringToBoolean(cf.getString("barDisplay"), true);
          boolean barDefault = StringUtils.stringToBoolean(cf.getString("barDefault"), false);
          String menu = cf.getString("menu");
          String name = cf.getString("name");
          String shortcuts = StringUtils.stringToString(cf.getString("shortcuts"), "");
          int sortOrder = Integer.parseInt(cf.getString("sortOrder"));
          MenuItem item = new MenuItem(dsd.getName(), name, "", menu, sortOrder, shortcuts,
                                       lineType, plotSeparately, barDisplay, barDefault, biasType);
          section.addMenuItem(item);
          items.put(item.menuId, item);
        }
      }
    }
  }

  // TODO: cache
  // TODO: sortOrder

  /**
   * Getter for sections.
   *
   * @return list of Sections associated with this menu handler
   */
  public List<Section> getSections() {
    ArrayList<Section> list = new ArrayList<Section>();
    for (Section section : sections.values()) {
      list.add(section);
    }

    return list;
  }

  /**
   * Getter for menu item from ID.
   *
   * @param id menu item id
   * @return menu item from internal list found by it's id
   */
  public MenuItem getItem(String id) {
    return items.get(id);
  }

  /**
   * Handle the given http request and generate an appropriate response.
   *
   * @see HttpHandler#handle
   */
  public Object handle(HttpServletRequest request) {
    return new Menu(getSections());
  }
}
