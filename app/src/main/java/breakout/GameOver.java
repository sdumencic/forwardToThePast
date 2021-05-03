package breakout;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
    /*Intent intent = new Intent(context, GameOver.class);
                intent.putExtra("points", points);
                        context.startActivity(intent);
                        ((Activity)context).finish(); DODAT U BREAKOUT_GAMEVIEW*/