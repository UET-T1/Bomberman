package main.engine;

// class GameEngine contatin and processing game loop
public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 60;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Timer timer;

    private final GameLogic gameLogic;

    private final Input input;

    // Construction.
    public GameEngine(String windowTitle, int width, int height, GameLogic gameLogic) throws Exception {
        window = new Window(width, height, "Bomman");
        input = window.getInputObj();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.create();
        timer.init();
        gameLogic.create(window);
    }

    // Fixed Step Game Loop
    protected void gameLoop() throws Exception {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_FPS;


        boolean running = true;
        while (running && !window.isClose()) {

            //find avg time
            Timer time = new Timer();
            time.init();
            //

            elapsedTime = timer.getTimeElapsed();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            //find avg time
            float x = (float) (time.getTime() - time.getLastLoopTime());
            if(x < 1/50f) {
                ObjectManager.totalDem += 1;
                ObjectManager.totalTime += x;
                ObjectManager.avgTimePerFrame = (ObjectManager.totalTime) / (ObjectManager.totalDem);
            }
            if (ObjectManager.totalTime > 10.0f) {
                ObjectManager.totalTime = 1.0f/60;
                ObjectManager.totalDem = 1;
            }
            // System.out.println(ObjectManager.avgTimePerFrame);
            // System.out.println((1.0f/60));
            // System.out.println();
        }
    }

    protected void cleanup() {
        gameLogic.destroy();                
    }

    protected void input() throws Exception {
        gameLogic.input(window, input);
    }

    protected void update(float interval) {
        gameLogic.update(interval, input, window);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
