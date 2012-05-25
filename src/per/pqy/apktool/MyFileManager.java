package per.pqy.apktool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyFileManager extends ListActivity {
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "/";
	private String curPath = "/";
	private TextView mPath;

	// private final static String TAG = "bb";

	// 对文件进行忽略大小写排序，并且文件夹在前，文件在后
	private class CaseInsensitiveOrderComparator implements Comparator<File> {
		public int compare(File lhs, File rhs) {
			if ((lhs.isDirectory() && rhs.isDirectory())
					|| (lhs.isFile() && rhs.isFile())) {
				String lfn = lhs.getName();
				String rfn = rhs.getName();
				return lfn.toLowerCase().compareTo(rfn.toLowerCase());
			} else {
				return lhs.isDirectory() ? -1 : 1;
			}
		}
	}

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.fileselect);
		mPath = (TextView) findViewById(R.id.mPath);
		Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent data = new Intent(MyFileManager.this,
						ApktoolActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("file", curPath);
				data.putExtras(bundle);
				setResult(2, data);
				finish();

			}
		});
		Button buttonCancle = (Button) findViewById(R.id.buttonCancle);
		buttonCancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		getFileDir(rootPath);
	}

	private void getFileDir(String filePath) {
		mPath.setText(getString(R.string.current_dir, filePath));
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		Arrays.sort(files, new CaseInsensitiveOrderComparator());

		if (!filePath.equals(rootPath)) {
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		setListAdapter(new MyAdapter(this, items, paths));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = new File(paths.get(position));
		if (file.isDirectory()) {
			curPath = paths.get(position);
			getFileDir(paths.get(position));
		} else {
			openFile(file);
		}
	}

	private void openFile(File f) {
		Intent data = new Intent(MyFileManager.this, ApktoolActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("file", f.getAbsolutePath());
		data.putExtras(bundle);
		setResult(2, data);
		finish();
	}
}