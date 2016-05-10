abstract class Command {
	public abstract void execute();
	public abstract void undo();
}

class Scheme extends Command {
	private Command[] actions;
	public Scheme(Command... actions) { this.actions = actions; }
	public void execute() { for (Command action : actions) action.execute(); }
	public void undo() { for (Command action : actions) action.undo(); }
}

interface Combatant {
	void startCombat();
	void stopCombat();
}

interface Rider {
	void rideToVictory();
	void retreatToMinasTirith();
}

class CombatCommand extends Command {
	private Combatant combatant;
	public CombatCommand(Combatant combatant) { this.combatant = combatant; }
	public void execute() { combatant.startCombat(); }
	public void undo() { combatant.stopCombat(); }
}

class RideCommand extends Command {
	private Rider rider;
	public RideCommand(Rider rider) { this.rider = rider; }
	public void execute() { rider.rideToVictory(); }
	public void undo() { rider.retreatToMinasTirith(); }
}

abstract class Actor {
	protected BattleForOsgiliath battle;
	public Actor(BattleForOsgiliath battle) { this.battle = battle; }
}

class Denethor extends Actor {
	private Command rideScheme;
	public Denethor(BattleForOsgiliath battle) { super(battle); }

	public void elaborateSchemes(BattleForOsgiliath battle) {
		System.out.println("Denethor elaborates his battle schemes...");

		rideScheme = new Scheme(
			new RideCommand(battle.faramir),
			new RideCommand(battle.horseman)
		);

		battle.faramir.attackPlan = new Scheme(
			new CombatCommand(battle.faramir),
			new CombatCommand(battle.horseman),
			new CombatCommand(battle.pippin)
		);
	}

	public void orderToRide() {
		System.out.println("Denethor orders his troops to ride to Osgiliath!");
		rideScheme.execute();
	}

	public void callTroopsBack() {
		System.out.println("Denethor calls his troops back");
		rideScheme.undo();
	}
}

class Faramir extends Actor implements Rider, Combatant {
	public Command attackPlan;
	public Faramir(BattleForOsgiliath battle) { super(battle); }

	public void rideToVictory() { System.out.println("Faramir rides to Osgiliath!"); }
	public void startCombat() { System.out.println("Faramir start fighting orcs!"); }
	public void stopCombat() { System.out.println("Faramir hides..."); }
	public void retreatToMinasTirith() { System.out.println("Faramir retreats to Minas Tirith!"); }

	public void spotOrc() {
		System.out.println("Faramir spots an evil orc!");
		attackPlan.execute();
	}

	public void realizeThereIsNoHope() {
		System.out.println("Wounded, Faramir realizes there is no hope!");
		attackPlan.undo();
	}
}

class Pippin extends Actor implements Combatant {
	public Pippin(BattleForOsgiliath battle) { super(battle); }

	public void startCombat() { System.out.println("Pippin starts singing..."); }
	public void stopCombat() { System.out.println("Pippin stops singing..."); }
}

class Horseman extends Actor implements Rider, Combatant {
	public Horseman(BattleForOsgiliath battle) { super(battle); }

	public void rideToVictory() { System.out.println("The Horseman rides to Osgiliath!"); }
	public void startCombat() { System.out.println("The Horseman start fighting orcs!"); }
	public void stopCombat() { System.out.println("The Horseman is killed while trying to retreat :("); }
	public void retreatToMinasTirith() { System.out.println("Dead horsemen can't retreat!"); }
}

public class BattleForOsgiliath {
	public final Denethor denethor = new Denethor(this);
	public final Faramir faramir = new Faramir(this);
	public final Pippin pippin = new Pippin(this);
	public final Horseman horseman = new Horseman(this);

	public void playScene() {
		denethor.elaborateSchemes(this);
		denethor.orderToRide();
		faramir.spotOrc();
		System.out.println("** Epic combat actions take place **");
		faramir.realizeThereIsNoHope();
		denethor.callTroopsBack();
		System.out.println("** THE END **");
	}

	public static void main(String... args) {
		new BattleForOsgiliath().playScene();
	}
}
