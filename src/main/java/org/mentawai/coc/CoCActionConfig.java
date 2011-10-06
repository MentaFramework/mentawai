package org.mentawai.coc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.log.Debug;

/**
 * <p>
 * A action config that uses Convention over Configuration. It is not nescessary
 * to configure the which action class will threat each url, neither which
 * consequence will be applied for each result.
 * </p>
 * <p>
 * The entire classpath will be scanned. All classes that ends with the "Action"
 * sufix will be loaded by the ActionConfig.
 * </p>
 * <p>
 * For define which consequences will be executed for each result, the
 * CoCActionConfig uses a CoCConsequenceProvider. The CoCConsequenceProvider
 * will provide a consequence based on it's own convention. The default
 * CoCConsequenceProvider implementation is the ForwardCoCConsequenceProvider,
 * but it can be changed by the setConsequenceProvider() method.
 * </p>
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * 
 */
public class CoCActionConfig extends ActionConfig {
	
	private static final String NAME = "CocActionConfig";

	private static final Map<String, Class<Action>> actionClassCache = new HashMap<String, Class<Action>>();

	private static final String webinfClasses = ApplicationManager
			.getRealPath()
			+ File.separator
			+ "WEB-INF"
			+ File.separator
			+ "classes"
			+ File.separator;

	private static void readDir(File dir) {

		File[] files = dir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				readDir(f);
			} else {
				String filename = f.getAbsolutePath();
				if (filename.endsWith("Action.class")) {
					String classname = filename.substring(webinfClasses
							.length(), filename.length() - 6);
					classname = classname.replace('/', '.');
					classname = classname.replace('\\', '.');
					Class klass = null;
					try {
						klass = Class.forName(classname);
						if (Action.class.isAssignableFrom(klass)) {
							actionClassCache.put(klass.getSimpleName(), klass);
						} else {
							Debug.log(NAME, "Class", klass.getName(), "end's with \'Action\' posfix, but is not a implemenof", Action.class.getName(), "interface.");
						}
					} catch (ClassNotFoundException e) {
						
						Debug.log(NAME, "Could not load class", classname, "exception =", e);
					}

				}
			}

		}
	}

	public static void loadAllActions() {
		File dir = new File(webinfClasses);
		if (dir.isDirectory()) {
			readDir(dir);
		}
	}

	static {
		loadAllActions();
	}

	private static ConsequenceProvider consequenceProvider = new ForwardConsequenceProvider();

	public CoCActionConfig(String name) {
		super(name, actionClassCache.get(name));

	}

	public Consequence getConsequence(String result) {

		Consequence consequence = super.getConsequence(result);
		if (consequence == null) {
			consequence = consequenceProvider.getConsequence(this.getName(), this.getActionClass(),
					result, null);
			this.addConsequence(result, consequence);
		}

		return consequence;
	}

	public Consequence getConsequence(String result, String innerAction) {
		Consequence consequence = super.getConsequence(result, innerAction);
		if (consequence == null) {
			consequence = consequenceProvider.getConsequence(this.getName(), this.getActionClass(),
					result, innerAction);
			this.addConsequence(result, innerAction, consequence);
		}

		return consequence;
	}

	public static void setConsequenceProvider(
			ConsequenceProvider consequenceProvider) {
		CoCActionConfig.consequenceProvider = consequenceProvider;
	}
}
