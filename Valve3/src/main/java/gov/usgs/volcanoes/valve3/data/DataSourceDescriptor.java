package gov.usgs.volcanoes.valve3.data;

import gov.usgs.volcanoes.core.configfile.ConfigFile;
import gov.usgs.volcanoes.valve3.Plotter;

/**
 * Keeps configuration parameters for vdx data source.
 *
 * @author Dan Cervelli
 */
public class DataSourceDescriptor {
  private String name;
  private String vdxClientName;
  private String vdxSource;
  private String plotterClassName;
  private ConfigFile config;

  /**
   * Constructor.
   *
   * @param n  name of valve data source ("source" config file parameter)
   * @param c  vdx name ("vdx" config file parameter)
   * @param s  vdx data source name ("vdx.source" config file parameter)
   * @param pc plotter name ("plotter" config file parameter)
   * @param cf subconfiguration for this data source
   */
  public DataSourceDescriptor(String n, String c, String s, String pc, ConfigFile cf) {
    name = n;
    vdxClientName = c;
    vdxSource = s;
    plotterClassName = pc;
    config = cf;
  }

  /**
   * Getter for name.
   *
   * @return name of this data source
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for VDX cleint name.
   *
   * @return vdx name for valve data source
   */
  public String getVDXClientName() {
    return vdxClientName;
  }

  /**
   * Getter for VDX source.
   *
   * @return vdx data source name
   */
  public String getVDXSource() {
    return vdxSource;
  }

  /**
   * Getter for config file.
   *
   * @return subconfiguration for valve data source
   */
  public ConfigFile getConfig() {
    return config;
  }

  /**
   * Getter for plotter.
   *
   * @return initialized plotter for valve data source
   */
  public Plotter getPlotter() {
    if (plotterClassName == null) {
      return null;
    }

    try {
      Plotter plotter = (Plotter) Class.forName(plotterClassName).newInstance();
      plotter.setVDXClient(vdxClientName);
      plotter.setVDXSource(vdxSource);
      plotter.setPlotterConfig(config.getSubConfig("plotter"));
      return plotter;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }
}
