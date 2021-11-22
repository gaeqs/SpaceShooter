package io.github.spaceshooter.counter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import io.github.spaceshooter.BaseFragment;
import io.github.spaceshooter.R;
import io.github.spaceshooter.ScaffoldActivity;
import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.StandardGameView;
import io.github.spaceshooter.space.scene.GameScene;
import io.github.spaceshooter.space.scene.PauseScene;


public class GameFragment extends BaseFragment implements View.OnClickListener {
    private GameEngine engine;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
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
                observer.removeOnGlobalLayoutListener(this);
                StandardGameView gameView = getView().findViewById(R.id.gameView);

                engine = new GameEngine(getActivity(), gameView);

                Scene scene = new GameScene(engine);
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
