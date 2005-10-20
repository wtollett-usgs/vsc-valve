package gov.usgs.valve3.result;

/**
 * 
 * $Log: not supported by cvs2svn $
 * Revision 1.1  2005/08/26 20:41:31  dcervelli
 * Initial avosouth commit.
 *
 * @author Dan Cervelli
 */
abstract public class Result
{
	protected String url;
	
	public String getURL()
	{
		return url;
	}
	
	public void setURL(String u)
	{
		url = u;
	}
	
	public void delete()
	{
		System.out.println("Result.delete()");
	}
	
	public String toXML(String type, String nested)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<valve3result>\n");
		sb.append("\t<type>" + type + "</type>\n");
		//sb.append("\t<url>" + url + "</url>\n");
		sb.append(nested + "\n");
		sb.append("</valve3result>\n");
		return sb.toString();
	}
	
	abstract public String toXML();
}
