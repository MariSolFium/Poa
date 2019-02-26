package sesion1;

import jade.core.Agent;
import jade.core.behaviours.*;

public class Agente2 extends Agent{
	/* Existen comportamientos que se ejecutan según el tiempo, tenemos que invertigarlo*/
	public void setup() {
		System.out.println("Hola mundo soy Skynet " + this.getName());
		addBehaviour(new OneShotBehaviour() { //solo se ejecuta una vez
			
			@Override
			public void action() {
				System.out.println("OneShotBehaviour()");
			}
		});
		
		addBehaviour(new CyclicBehaviour() { //sigue ejecutando hasta que lo paremos
			int state=0;
			@Override
			public void action() {
				
				switch (state) {
				case 0:
					System.out.println("CyclicBehaviour("+state+")");
					state =1;
					break;
				case 1:
					System.out.println("CyclicBehaviour("+state+")");
					state =0;
					break;
				default:
					break;
				}
				System.out.println("CyclicBehaviour()");				
			}
		});
	}
	
}
