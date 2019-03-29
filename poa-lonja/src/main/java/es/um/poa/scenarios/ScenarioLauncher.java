package es.um.poa.scenarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

import es.um.poa.agents.clock.ClockAgentConfig;
import es.um.poa.utils.AgentLoggingHTMLFormatter;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

public class ScenarioLauncher {
	public static void main(String[] args) throws SecurityException, IOException {
		if(args.length == 1) { //hay que pasarle al main el fich de configuración para que cree un objeto yaml con yaml.load y con los métodos gets se obtienen los valores de los escenarios
			String config_file = args[0];
			Yaml yaml = new Yaml();
			InputStream inputStream = new FileInputStream(config_file);
			ScenarioConfig scenario = yaml.load(inputStream);
			
			initLogging();
			
			System.out.println(scenario);
			try {

				// Obtenemos una instancia del entorno runtime de Jade
				Runtime rt = Runtime.instance();
				// Terminamos la mÃ¡quinq virtual si no hubiera ningÃºn contenedor de agentes activo
				rt.setCloseVM(true);
				// Lanzamos una plataforma en el puerto 8888
				// Y creamos un profile de la plataforma a partir de la cual podemos
				// crear contenedores
				Profile pMain = new ProfileImpl(null, 8888, null);
				System.out.println("Lanzamos una plataforma desde clase principal..."+pMain);
				
				// Creamos el contenedor
				AgentContainer mc = rt.createMainContainer(pMain);
				
				// Creamos un RMA (la GUI de JADE)
				System.out.println("Lanzando el agente RMA en el contenedor main ...");
				AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
				rma.start();

				// INICIALIZACIÃ“N DE LOS AGENTES
				
				// ClockAgent
				ClockAgentConfig cc = scenario.getClock();
				Object[] arguments =  {cc.getUnitTimeMillis(), cc.getNumUnitDay(), cc.getNumSimDays()};
				
				AgentController clock = mc.createNewAgent("clock", es.um.poa.agents.clock.ClockAgent.class.getName(), arguments);
				clock.start();
				
				// FishMarket
				AgentRefConfig marketConfig = scenario.getFishMarket();
				Object[] marketConfigArg = {marketConfig.getConfig()};
				AgentController market = mc.createNewAgent(
						marketConfig.getName(), 
						es.um.poa.agents.fishmarket.FishMarketAgent.class.getName(), //tipo agente
						marketConfigArg); //y pasado el fichero de configuración
				market.start();

				// Buyers
				List<AgentRefConfig> buyers = scenario.getBuyers();
				for(AgentRefConfig buyer: buyers) {
					System.out.println(buyer);
					Object[] buyerConfigArg = {buyer.getConfig()};
					AgentController b = mc.createNewAgent(
							buyer.getName(), 
							es.um.poa.agents.buyer.BuyerAgent.class.getName(), 
							buyerConfigArg);
					b.start();
				}
				
				// Sellers
				List<AgentRefConfig> sellers = scenario.getSellers();
				for(AgentRefConfig seller: sellers) {
					System.out.println(seller);
					Object[] buyerConfigArg = {seller.getConfig()};
					AgentController b = mc.createNewAgent(
							seller.getName(), 
							es.um.poa.agents.seller.SellerAgent.class.getName(), 
							buyerConfigArg);
					b.start();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void initLogging() throws SecurityException, IOException {
	      LogManager lm = LogManager.getLogManager();
	      Logger parentLogger, childLogger;
	      FileHandler html_handler = new FileHandler("log_output.html");
	      parentLogger = Logger.getLogger("");

	      lm.addLogger(parentLogger);
	      parentLogger.setLevel(Level.INFO);
	      html_handler.setFormatter(new AgentLoggingHTMLFormatter());
	}
}
