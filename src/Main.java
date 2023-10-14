import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public class Main {

//    DOWNLOAD Raylib-J from https://github.com/CreedVI/Raylib-J/releases/download/v0.5/Raylib-J.jar
//    Include as Library
    public static void main(String[] args){
        Raylib rlj = new Raylib();
        rlj.core.InitWindow(800, 600, "Raylib-J Example");

        while (!rlj.core.WindowShouldClose()){
            rlj.core.BeginDrawing();
            rlj.core.ClearBackground(Color.WHITE);
            rlj.text.DrawText("Hello, World!", 800 - (rlj.text.MeasureText("Hello, World!", 20)*2), 300, 20, Color.DARKGRAY);
            rlj.shapes.DrawCircle(400, 300, 20, Color.DARKGRAY);
            rlj.core.EndDrawing();
        }
    }
}
