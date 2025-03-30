package controller;
import java.util.Random;
import java.util.concurrent.Semaphore;
public class practice {

	private static final int teamqty = 7;
	private static final int carqty = 2; // carros por equipe
	private final int lapqty = 3;
	
	
	private static final Semaphore track = new Semaphore(5); // ate 5 carros de uma vez
	private final Semaphore[] teamsemaphore = new Semaphore[teamqty];
	
	
	public practice() {
		for(int i = 0; i < teamqty; i++) {
			teamsemaphore[i] = new Semaphore(1); // somente um carro por equipe na pista
			
		}
	}

	public void startpractice() {
		for(int i = 1; i <= teamqty; i++) {
			int teamid = i;
			for(int j = 1; j <= carqty ; j++) {
				int carid = j;
				Thread driver = new Thread(() -> lapsimulation(teamid, carid));
				driver.start();
			}
		}
	}
	public void lapsimulation(int teamid, int carid) {
		try {
			teamsemaphore[teamid -1].acquire();
			
			track.acquire();
			System.out.println("carro " + carid + " da equipe " + teamid + " entrou na pista.");
			
			Random random = new Random();
			int bestime = Integer.MAX_VALUE;
			
			for(int i = 0; i < lapqty ; i++) {
				int time = random.nextInt(50)+50; // tempo entre 50 e 100 ms
				System.out.println("carro " + carid + " da equipe " + teamid + " completou a volta " + i + " em " + time + "ms.");
				if(time < bestime) {
					bestime = time;
				}
			}
			System.out.println("Carro " + carid + " da Equipe " + teamid + " registrou sua melhor volta em " + bestime + "ms.");
            System.out.println("Carro " + carid + " da Equipe " + teamid + " saiu da pista");
            
            // liberando a pista e a equipe
            track.release();
            teamsemaphore[teamid -1].release();    
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}
