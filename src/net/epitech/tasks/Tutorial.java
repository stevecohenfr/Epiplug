package net.epitech.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Tutorial extends BukkitRunnable {
	private Player					target;
	private final static int 		TIME_BEFORE = 10; //Time before tutorial start
	private final static int		TIME_BEFORE_OFFSET = 5; //Time before show TIME_BEFORE countdown

	private static Block			START_POS;
	private static List<Block>		GO_FORWARD;
	private static List<Block>		GO_RIGHT;
	private static List<Block>		GO_LEFT;
	private static List<Block>		LADDER;
	private static String			OPEN_DOORS;
	private static List<Block>		SWIMMING;

	public Tutorial(Player player) {
		this.target = player;
	}

	public void initValuesEnDureFaconGrosDegeulasse() {
		//TODO
		START_POS = new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock();
		//TODO
		GO_FORWARD = new ArrayList<Block>(4);
		GO_FORWARD.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_FORWARD.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_FORWARD.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_FORWARD.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		//TODO
		GO_RIGHT = new ArrayList<Block>(4);
		GO_RIGHT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_RIGHT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_RIGHT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_RIGHT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		//TODO
		GO_LEFT = new ArrayList<Block>(4);
		GO_LEFT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_LEFT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_LEFT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		GO_LEFT.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		//TODO
		LADDER = new ArrayList<Block>(4);
		LADDER.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		LADDER.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		LADDER.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		LADDER.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		//TODO
		OPEN_DOORS = "Bonjour";
		//TODO
		SWIMMING = new ArrayList<Block>(4);
		SWIMMING.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		SWIMMING.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		SWIMMING.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
		SWIMMING.add(new Location(Bukkit.getWorld("Tutorial"), 0, 0, 0).getBlock());
	}

	public void startTimer() throws InterruptedException {
		for (int i = TIME_BEFORE - 5; i > 0; --i) {
			target.sendMessage(ChatColor.RED + String.valueOf(i));
			Thread.sleep(i * 1000);
		}
	}

	public void startTuto() {
		//Check player location == cubes in list location and change state
	}

	@Override
	public void run() {
		try {
			target.sendMessage(ChatColor.RED + "Si vous voulez suivre un court tutorial, ne touchez à rien et attendez 10 secondes");
			Thread.sleep(TIME_BEFORE_OFFSET * 1000);
			startTimer();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	enum State {
		FORWARD(0) {
			public State setForward() {
				System.err.println("Already FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Turning RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Turning LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Turning LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Turning DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Turning SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Turning END");
				return END;
			}
		},
		RIGHT(1) {
			public State setForward() {
				System.err.println("Turning FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Already RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Turning LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Turning LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Turning DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Turning SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Turning END");
				return END;
			}
		},
		LEFT(2) {
			public State setForward() {
				System.err.println("Turning FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Turning RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Already LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Turning LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Turning DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Turning SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Turning END");
				return END;
			}
		},
		LADDERS(3) {
			public State setForward() {
				System.err.println("Turning FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Turning RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Turning LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Already LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Turning DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Turning SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Turning END");
				return END;
			}
		},
		DOORS(4) {
			public State setForward() {
				System.err.println("Turning FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Turning RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Turning LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Turning LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Already DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Turning SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Turning END");
				return END;
			}
		},
		SWIMMING(5) {
			public State setForward() {
				System.err.println("Turning FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Turning RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Turning LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Turning LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Turning DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Already SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Turning END");
				return END;
			}
		},
		END(6) {
			public State setForward() {
				System.err.println("Turning FORWARD");
				return FORWARD;
			}
			public State setRight() {
				System.err.println("Turning RIGHT");
				return RIGHT;
			}
			public State setLeft() {
				System.err.println("Turning LEFT");
				return LEFT;
			}
			public State setLadders() {
				System.err.println("Turning LADDERS");
				return LADDERS;
			}
			public State setDoors() {
				System.err.println("Turning DOORS");
				return DOORS;
			}
			public State setSwimming() {
				System.err.println("Turning SWIMMING");
				return SWIMMING;
			}
			public State setEnd() {
				System.err.println("Already END");
				return END;
			}
		};

		private int val;
		State(int val) {
			this.val = val;
		}

		public abstract State setForward();
		public abstract State setRight();
		public abstract State setLeft();
		public abstract State setLadders();
		public abstract State setDoors();
		public abstract State setSwimming();
	}
}
