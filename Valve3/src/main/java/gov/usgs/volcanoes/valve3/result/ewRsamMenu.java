package gov.usgs.volcanoes.valve3.result;

/**
 * Determine if both events and counts tables are present and inform the GUI.
 *
 * @author Tom Parker
 */
public class ewRsamMenu extends Result {
  public String title = "EWRsam Data";
  String dataTypes;

  /**
   * Constructor.
   *
   * @param src list of data types
   */
  public ewRsamMenu(java.util.List<String> src) {
    logger.info("ewRsamMenu() src = {}", src.toString());

    dataTypes = src.toString();
  }

  /**
   * Yield XML representation.
   *
   * @return XML representation of this ewRsamMenu
   */
  public String toXml() {
    StringBuilder sb = new StringBuilder(256);
    sb.append("\t<ewRsamMenu>\n");
    sb.append("\t\t<dataTypes>" + dataTypes + "</dataTypes>\n");
    sb.append("\t</ewRsamMenu>\n");
    return toXml("ewRsamMenu", sb.toString());
  }
}
