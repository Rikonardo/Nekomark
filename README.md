# Nekomark (=^･ω･^=)
Simple JVM benchmarking tool. Test startup time, computing performance, JNI and reflection calls overhead, etc.  

## Usage
Download release or build it by yourself, then run using JVM you want to test. Possible launch arguments:

| Argument            | Description                                                | Default value           |
|---------------------|------------------------------------------------------------|-------------------------|
| `--iterations`      | How much benchmarking iterations should be done            | `3`                     |
| `--jvm-args`        | JVM arguments that should be passed to tested JVM instance | `"-Xms512m -Xmx512m"`   |
| `--enable-jni-test` | Test JNI calls overhead                                    | `false`                 |
| `--output`          | Name for benchmark report file                             | `"nekomark-report.txt"` |

## JNI testing requirements
To use JNI test, you need to have Golang compiler installed. It is used to compile native library required by this test.
You can download latest GO version from [Golang website](https://go.dev/dl/).

**Notice: JNI test will not work correctly if your OS architecture does not match tested JVM build architecture.**

## Understanding benchmark source code
Benchmark consists of two parts: launcher and benchmark itself.
Launcher is responsible for starting JVM, receiving benchmarking results and generating report file.
Benchmarking part runs list of different tests and reports its results back to launcher via standard output.
