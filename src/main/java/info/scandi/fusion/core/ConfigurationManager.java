package info.scandi.fusion.core;

import java.io.File;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.ConfigurationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import info.scandi.fusion.conf.Browser;
import info.scandi.fusion.conf.Common;
import info.scandi.fusion.conf.Database;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.utils.OSType;

@Named
@ApplicationScoped
public class ConfigurationManager {

	private info.scandi.fusion.conf.Fusion fusion;

	public ConfigurationManager() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("info.scandi.fusion.conf");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		this.fusion = (info.scandi.fusion.conf.Fusion) unmarshaller.unmarshal(new File("fusion.xml"));

	}

	public Database getDatabase() {
		return fusion.getDatabase();
	}

	public Common getCommon() {
		return fusion.getCommon();
	}

	public Browser getBrowser() {
		return fusion.getBrowser();
	}

	public String getInitFile() {
		return getCommon().getRootPath().concat("flatXmlDataSet").concat(getDatabase().getInit().getInitFile());
	}

	public String getBackupDirectory() {
		String BDir = getDatabase().getBackup().getBackupDirectory() != null
				? getDatabase().getBackup().getBackupDirectory()
				: "/save";
		return getCommon().getRootPath().concat("flatXmlDataSet").concat(BDir);
	}

	public OSType getOSType() throws FusionException {
		String osName = System.getProperty("os.name");
		String osNameMatch = osName.toLowerCase(Locale.FRANCE);
		if (osNameMatch.contains("linux")) {
			return OSType.LINUX;
		} else if (osNameMatch.contains("windows")) {
			return OSType.WINDOWS;
		} else if (osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
			return OSType.MACOS;
		} else {
			throw new FusionException("Unsupported OS");
		}
	}

	public Integer getImpliciteWait() {
		return 1000;
	}

	public Integer getExpliciteWait() {
		return 10;
	}

	public Integer getPageLoadTimeout() {
		return 10000;
	}

	public Integer getScriptTimeout() {
		return 10;
	}

	public String getDatabasePrefix() throws ConfigurationException {
		String prefix = "";
		switch (this.getDatabase().getType()) {
		case "postgresql":
			prefix = "jdbc:postgresql://";
			break;

		case "oracle":
			prefix = "jdbc:oracle:thin:@";
			break;
		default:
			throw new ConfigurationException("Unsupported database type.");
		}
		return prefix;
	}
}
