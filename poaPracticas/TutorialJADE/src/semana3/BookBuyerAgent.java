package semana3;

import jade.core.AID;
import jade.core.Agent;

public class BookBuyerAgent extends Agent {

	private String targetBookTittle;

	private AID[] sellerAgents = { new AID("seller1", AID.ISLOCALNAME), new AID("seller2", AID.ISLOCALNAME) };

	public void setup() {
		
		System.out.println("Hello! Buyer-agent "+getAID().getName()+" is ready.");
		
		Object[] args = getArguments();
		
		if(args!=null && args.length>0) {
			
			int i=0;
			while(i<args.length) {
				targetBookTittle=(String)args[i];
				System.out.println("Trying to buy " + targetBookTittle);
				i++;
			}
		} else {
			System.out.println("No book title specified");
		}
	}
}
