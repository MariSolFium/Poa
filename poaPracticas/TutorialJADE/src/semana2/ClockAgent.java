package semana2;


import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class ClockAgent extends Agent {
/** Prueba para git  **/
	public void setup() {
		Object[] args = getArguments();
		
		if(args.length==3) {
			int millisPerUnit= Integer.parseInt((String) args[0]);
			int unitPerDay= Integer.parseInt((String)args[1]);
			int nD= Integer.parseInt((String)args[2]);
			System.out.println(millisPerUnit+"," + unitPerDay+ "," + nD);
			
			
			
			/*Ejemplo de como se controlaría el tiempo*/
			addBehaviour(new TickerBehaviour(this, millisPerUnit) {
				int numUnit=0;
				int numDays=0;
				
				@Override
				protected void onTick() {
					System.out.println("Se actualiza el tiempo");
					/*Cada tick hay que incrementar +1 la unidad de tiempo*/
					numUnit++;
					System.out.println(numUnit);
					if(numUnit>unitPerDay) {
						System.out.println("Pasa el día: " + numUnit + ", " + numDays);
						numDays++;
						numUnit=0;
					}
					
					if(numDays==nD){
						System.out.println("Pasaron " +numDays+ " días.");
						getAgent().doDelete();
					}
				}
				
				//En vez de hacer printf utilizar la herramienta de log de Jade.
			});
		}
			
	}
	

}
