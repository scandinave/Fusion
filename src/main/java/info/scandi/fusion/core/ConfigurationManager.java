package info.scandi.fusion.core;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import info.scandi.fusion.conf.Browser;
import info.scandi.fusion.conf.Common;
import info.scandi.fusion.conf.Database;

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
				: "/save.xml";
		return getCommon().getRootPath().concat("flatXmlDataSet").concat(BDir);
	}
}
