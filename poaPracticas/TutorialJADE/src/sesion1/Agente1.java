package sesion1;

import jade.core.Agent;
import jade.core.behaviours.*;

public class Agente1 extends Agent{
	
	public void setup() {
		System.out.println("Hola mundo soy Skynet " + this.getName());
		addBehaviour(new Behaviour() {
			
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public void action() {
				System.out.println("Hola, paso de nuevo que me estoy ejecutando");
			}
		});	
	}
	
}
