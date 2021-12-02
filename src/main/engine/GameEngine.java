package main.engine;

// class GameEngine contatin and processing game loop
public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Timer timer;

    private final GameLogic gameLogic;

    private final Input input;

    // Construction.
    public GameEngine(String windowTitle, int width, int height, GameLogic gameLogic) throws Exception {
        window = new Window(width, height, "Bomman");
        input = new Input();
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
    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.isClose()) {
            elapsedTime = timer.getTimeElapsed();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();
        }
    }

    protected void cleanup() {
        gameLogic.destroy();                
    }

    protected void input() {
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
