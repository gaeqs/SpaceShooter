package io.github.spaceshooter.counter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import io.github.spaceshooter.BaseFragment;
import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.StandardGameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.Component;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.scene.MainMenuScene;


public class GameFragment extends BaseFragment implements View.OnClickListener {
    private GameEngine engine;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Para evitar que sea llamado múltiples veces,
                //se elimina el listener en cuanto es llamado
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                StandardGameView gameView = getView().findViewById(R.id.gameView);

                engine = new GameEngine(getActivity(), gameView);

                Scene scene = new MainMenuScene(engine);
                engine.setScene(scene);
                engine.resume();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        engine.destroy();
    }

    @Override
    public void onClick(View v) {

    }
}
