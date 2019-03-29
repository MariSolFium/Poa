package es.um.poa.agents;

import es.um.poa.agents.clock.SimTimeOntology;
import es.um.poa.utils.OntologyFactory;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

//Esta es para los compradores?, para cuando se registren, etc
public class TimePOAAgent  extends POAAgent {
	private SimTimeOntology simTime;
	
	public void setup() {
		super.setup();
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("simulated-time");
		template.addServices(sd);
		Behaviour clockDFSub =  new SubscriptionInitiator(
				this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, null)) {
			protected void handleInform(ACLMessage inform) {
				try {
					DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
					if(dfds.length > 0) {
						AID clockAgent = dfds[0].getName();
						ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
						request.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
						request.setReplyWith(""+System.currentTimeMillis());
						request.addReceiver(clockAgent);
						
						myAgent.addBehaviour(new TimerUpdaterBehaviour(getAgent(), request));
					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		};
		
		addBehaviour(clockDFSub);
	}
	
	private class TimerUpdaterBehaviour extends SubscriptionInitiator {
		
		public TimerUpdaterBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			String content = inform.getContent();
			SimTimeOntology sto = OntologyFactory.getSimTimeOntologyObject(content);
			
			((POAAgent)getAgent()).getLogger().info("TimerUpdaterBehaviour", sto.toString());
			simTime = sto;
			getLogger().info("TimerUpdaterBehaviour", simTime.toString());
			if(simTime.getSimState().equals(SimTimeOntology.END)) {
				doDelete(); //el agente muere cuando termina la simulaci�n
			}
		}
	}
	
	public SimTimeOntology getSimTime() {
		return simTime; //actualiza el tiempo aqu�
	}
}
