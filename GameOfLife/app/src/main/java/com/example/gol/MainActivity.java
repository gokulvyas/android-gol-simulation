package com.example.gol;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 * @author Gokul
 *
 */
public class MainActivity extends Activity {
	private ArrayList<Point> livePoints;
	private TextView[][] twoDMainGrid;
	private ToggleButton playPauseButton;
	private Button clearButton;
	private AsyncTask<Void, Void, Void> generationTask;
	private TableLayout tableLayout;

	private static final int TOTAL_ROW = 20;
	private static final int TOTAL_COL = 20;
	private static int SLEEP_MILLISECONDS = 300;
	private static final String TEXT_LIVE_CELL = "*";
	private static final String TEXT_DEAD_CELL = " ";
	private static final String SAVED_INSTANCE_KEY = "KEY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(R.color.custom_dark_gray));

		// TableLayout is the ViewGroup for 2 dimensional cell grid with 20 Rows and 20 columns
		tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
		twoDMainGrid = new TextView[TOTAL_ROW][TOTAL_COL];

		for (int r = 0; r < TOTAL_ROW; r++) {
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new TableLayout.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT,
					TableLayout.LayoutParams.WRAP_CONTENT));

			// Create text views
			for (int c = 0; c < TOTAL_COL; c++) {
				createTextView(r, c);

				tableRow.addView(twoDMainGrid[r][c]);
			}
			tableLayout.addView(tableRow);
		}

		// If screen orientation changed, restore state of cells
		if (savedInstanceState != null) {
			restoreLiveNeigbours(savedInstanceState);
		}
		setButtons();
		addListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (generationTask != null)
			this.generationTask.cancel(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		livePoints = new ArrayList<Point>();
		for (int i = 0; i < MainActivity.TOTAL_ROW; i++) {
			for (int j = 0; j < MainActivity.TOTAL_COL; j++) {
				if (TEXT_LIVE_CELL.equals(this.twoDMainGrid[i][j].getText())) {
					livePoints.add(new Point(i, j));
				}
			}
		}
		// Save the live cells to support Screen Orientation
		if (livePoints != null && livePoints.size() > 0)
			outState.putParcelableArrayList(SAVED_INSTANCE_KEY, livePoints);
		super.onSaveInstanceState(outState);
	}

	private void createTextView(int r, int c) {
		twoDMainGrid[r][c] = new TextView(this);
		int w = getResources().getInteger(R.integer.width);
		int h = getResources().getInteger(R.integer.height);
		twoDMainGrid[r][c].setLayoutParams(new TableRow.LayoutParams(w, h));
		twoDMainGrid[r][c].setTextColor(new ColorDrawable(
				R.color.custom_dark_gray).getColor());
		twoDMainGrid[r][c].setText(TEXT_DEAD_CELL);
		twoDMainGrid[r][c].setClickable(true);
		twoDMainGrid[r][c].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView t = (TextView) v;
				if (t.getText() == TEXT_DEAD_CELL) {
					t.setText(TEXT_LIVE_CELL);
				} else {
					t.setText(TEXT_DEAD_CELL);
				}
			}

		});
	}

	private void restoreLiveNeigbours(Bundle savedInstanceState) {
		ArrayList<Point> list = (ArrayList<Point>) savedInstanceState
				.get(SAVED_INSTANCE_KEY);
		if (list != null)
			for (Point point : list) {
				twoDMainGrid[point.x][point.y].setText(TEXT_LIVE_CELL);
			}
	}

	private void setButtons() {
		playPauseButton = (ToggleButton) findViewById(R.id.playpause);
		clearButton = (Button) findViewById(R.id.clear);
	}

	private void addListeners() {
		playPauseButton
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							pause();
						} else {
							play();
						}
					}
				});
		clearButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				clear();
			}

		});
	}
	protected void clear() {
		pause();
		if (twoDMainGrid != null)
			for (int i = 0; i < MainActivity.TOTAL_ROW; i++) {
				for (int j = 0; j < MainActivity.TOTAL_COL; j++) {
					twoDMainGrid[i][j].setText(TEXT_DEAD_CELL);
				}
			}
	}

	protected void pause() {
		if (generationTask != null) {
			generationTask.cancel(true);
			generationTask = null;
		}

	}

	protected void play() {
		if (generationTask == null) {
			generationTask = new GenerationTask().execute();
		}
	}

	/**
	 * 
	 * Task that runs on another thread to check all the 4 rules of Life.
	 * This task maintains its own copy of cell state.
	 *
	 */
	public class GenerationTask extends AsyncTask<Void, Void, Void> {
		private int liveNeighbours;
		int[][] tempGrid;

		@Override
		protected Void doInBackground(Void... params) {

			while (!this.isCancelled()) {
				tempGrid = new int[MainActivity.TOTAL_ROW][MainActivity.TOTAL_COL];

				for (int i = 0; i < MainActivity.TOTAL_ROW; i++) {
					for (int j = 0; j < MainActivity.TOTAL_COL; j++) {
						liveNeighbours = getLiveNeighbours(i, j);
						boolean isAlive = twoDMainGrid[i][j].getText().equals(
								TEXT_LIVE_CELL);
						tempGrid[i][j] = isAlive ? 1 : 0;

						if (isAlive
								&& (liveNeighbours < 2 || liveNeighbours > 3)) {
							// Rule 1. live cell with fewer than two live
							// neighbours dies, as if caused by
							// under-population.
							// Rule 3. live cell with more than three live
							// neighbours dies, as if by overcrowding.
							tempGrid[i][j] = 0;
						}
						if (!isAlive && liveNeighbours == 3) {
							// Rule 4. dead cell with exactly three live
							// neighbours becomes a live cell, as if by
							// reproduction.
							tempGrid[i][j] = 1;
						}
						// Rule 2. live cell with two or three live neighbours
						// lives on to the next generation.
					}

				}
				publishProgress();
				try {
					Thread.sleep(MainActivity.SLEEP_MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			for (int i = 0; i < MainActivity.TOTAL_ROW; i++) {
				for (int j = 0; j < MainActivity.TOTAL_COL; j++) {
					twoDMainGrid[i][j].setText(tempGrid[i][j] == 1
							? TEXT_LIVE_CELL
							: TEXT_DEAD_CELL);
				}
			}
		}
		private int getLiveNeighbours(int i, int j) {
			int liveNeighbourCount = 0;

			for (int row = i - 1; row <= i + 1; row++) {
				for (int col = j - 1; col <= j + 1; col++) {
					int modOfRow = (((row % MainActivity.TOTAL_ROW) + MainActivity.TOTAL_ROW) % MainActivity.TOTAL_ROW);
					int modOfCol = (((col % MainActivity.TOTAL_COL) + MainActivity.TOTAL_COL) % MainActivity.TOTAL_COL);
					if (twoDMainGrid[modOfRow][modOfCol].getText().equals(
							TEXT_LIVE_CELL)
							&& !(modOfRow == i && modOfCol == j)) {
						liveNeighbourCount++;
					}
				}
			}
			return liveNeighbourCount;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

}
