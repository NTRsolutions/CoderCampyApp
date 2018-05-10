package com.gmonetix.codercampy.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.gmonetix.codercampy.R;

import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.adapters.Options;
import io.github.kbiakov.codeview.highlight.ColorTheme;
import io.github.kbiakov.codeview.highlight.Font;

public class CodeEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_editor);

        Crashlytics.getInstance().crash();

        CodeView codeView = (CodeView) findViewById(R.id.code_view);

        codeView.setOptions(Options.Default.get(this)
                .withLanguage("java")
                .withCode("package com.gmonetix.codercampy;\n" +
                        "\n" +
                        "import android.app.Application;\n" +
                        "import android.support.v7.app.AppCompatDelegate;\n" +
                        "\n" +
                        "import com.gmonetix.codercampy.util.SharedPref;\n" +
                        "import com.google.firebase.auth.FirebaseAuth;\n" +
                        "\n" +
                        "import io.github.kbiakov.codeview.classifier.CodeProcessor;\n" +
                        "\n" +
                        "/**\n" +
                        " * Created by Gaurav Bordoloi on 2/14/2018.\n" +
                        " */\n" +
                        "\n" +
                        "public class App extends Application {\n" +
                        "\n" +
                        "    static {\n" +
                        "        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);\n" +
                        "    }\n" +
                        "\n" +
                        "    private static FirebaseAuth auth;\n" +
                        "    private static SharedPref sharedPref;\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void onCreate() {\n" +
                        "        super.onCreate();\n" +
                        "\n" +
                        "        sharedPref = new SharedPref(getApplicationContext());\n" +
                        "\n" +
                        "        auth = FirebaseAuth.getInstance();\n" +
                        "\n" +
                        "        CodeProcessor.init(this);\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "    public static FirebaseAuth getAuth() {\n" +
                        "        return auth;\n" +
                        "    }\n" +
                        "\n" +
                        "    public static SharedPref getSharedPref() {\n" +
                        "        return sharedPref;\n" +
                        "    }\n" +
                        "\n" +
                        "}\n")
                .withFont(Font.Consolas)
                .withTheme(ColorTheme.MONOKAI));

    }
}
