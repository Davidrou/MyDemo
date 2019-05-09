package com.example.luxiaolin.handlerdemo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luxiaolin.handlerdemo.network.MyNetApi;
import com.example.luxiaolin.handlerdemo.network.bean.Translation;
import com.example.luxiaolin.handlerdemo.network.bean.Translation2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    int i = 0;

    private Button mButton;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateIntervel();
            }
        });
    }


    private void sample2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 2. 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍
                // a. 定义：事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }


        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件作出响应" + value);
            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }

    private void sample1() {

        //步骤1：创建被观察者 （Observable ）& 生产事件
        // 1. 创建被观察者 Observable 对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            // 2. 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍
                // a. 定义：事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件
                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
            }


        });

        //步骤2：创建观察者 （Observer ）并 定义响应事件的行为
        // 1. 创建观察者 （Observer ）对象
        Observer<Integer> observer = new Observer<Integer>() {
            // 2. 创建对象时通过对应复写对应事件方法 从而 响应对应事件
            // 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件作出响应" + value);
                i++;
            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };

        // 在Function函数中，必须对输入的 Observable<Object>进行处理，此处使用flatMap操作符接收上游的数据

        observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
            // 以此决定是否重新订阅 & 发送原来的 Observable，即轮询
            // 此处有2种情况：
            // 1. 若返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable，即轮询结束
            // 2. 若返回其余事件，则重新订阅 & 发送原来的 Observable，即继续轮询
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object throwable) throws Exception {

                        // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                        if (i > 3) {
                            // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
                            return Observable.error(new Throwable("轮询结束"));
                        }
                        // 若轮询次数＜4次，则发送1Next事件以继续轮询
                        // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                        return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
                    }
                });
            }
        });


        //步骤3：通过订阅（Subscribe）连接观察者和被观察者
        observable.subscribe(observer);
    }

    private void sampleInterval() {
        Observable.interval(2, 1, TimeUnit.SECONDS).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {

            }
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext" + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {

                Log.d(TAG, "onComplete");
            }
        });
    }

    class Student {
        public List<String> courses = new ArrayList<>();
        public String mName;

        public Student(String name) {
            courses.add("Math");
            courses.add("English");
            courses.add("Coding");
            mName = name;
        }
    }

    void sampleMap() {
        Observable.just(new Student("david"), new Student("jack")).map(new Function<Student, String>() {
            @Override
            public String apply(Student student) throws Exception {
                return student.mName;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete:");
            }
        });
    }

    void sampleFlatMap() {
        Observable.just(new Student("david"), new Student("jack")).flatMap(new Function<Student, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Student student) throws Exception {
                return Observable.fromIterable(student.courses);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete:");
            }
        });
    }

    public void translate() {
        //进行一次网络请求并返回数据
        Observable<Translation> observable = MyNetApi.getInterface().translate();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Translation translation) {
                        Log.d(TAG, "onNext:" + translation);
                        Toast.makeText(getApplicationContext(), "onNext " + translation.content.out, 0).show();
                        translation.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }

    public void translate2() {
        //进行一次网络请求并返回数据 使用Consumer形式
        Observable<Translation> observable = MyNetApi.getInterface().translate();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Translation>() {
                    @Override
                    public void accept(Translation translation) {
                        Log.d(TAG, "onNext:" + translation);
                        Toast.makeText(getApplicationContext(), "onNext " + translation.content.out, 0).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getApplicationContext(), "onError " + throwable.getMessage(), 0).show();
                    }
                });

    }

    public void translateRepeat() {
        Observable<Translation> observable = MyNetApi.getInterface().translate();
        // 步骤4：发送网络请求 & 通过repeatWhen（）进行轮询
        observable
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    // 在Function函数中，必须对输入的 Observable<Object>进行处理，此处使用flatMap操作符接收上游的数据
                    public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                        // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
                        // 以此决定是否重新订阅 & 发送原来的 Observable，即轮询
                        // 此处有2种情况：
                        // 1. 若返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable，即轮询结束
                        // 2. 若返回其余事件，则重新订阅 & 发送原来的 Observable，即继续轮询
                        return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Object throwable) throws Exception {

                                // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                                if (i > 3) {
                                    // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
                                    return Observable.error(new Throwable("轮询结束"));
                                }
                                // 若轮询次数＜4次，则发送1Next事件以继续轮询
                                // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                                return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
                            }
                        });

                    }
                })
                .subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求结果
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Translation result) {
                        // e.接收服务器返回的数据
                        result.show();
                        i++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 获取轮询结束信息
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void translateIntervel() {
        final Disposable[] disposable = new Disposable[1];
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        Log.d(TAG, "第 " + aLong + " 次轮询");
//                        translate();
//                    }
//                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "第 " + aLong + " 次轮询");
                        if (aLong >= 5) {
                            disposable[0].dispose();
                            return;
                        }
                        translate();
                        Log.d(TAG, "对onNext事件作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    public void translateOneByOne(){
        Observable<Translation> observable = MyNetApi.getInterface().translate();
        Observable<Translation2> observable2 = MyNetApi.getInterface().translate2();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Translation>() {
                    @Override
                    public void accept(Translation translation) throws Exception {
                        Toast.makeText(getApplicationContext(), "accept " + translation.content.out, 0).show();
                    }
                })
                .map(new Function<Translation, Translation2>() {
                    @Override
                    public Translation2 apply(Translation translation) throws Exception {
                        return null;
                    }
                })
                .subscribe(new Observer<Translation2>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation2 translation) {
                        Toast.makeText(getApplicationContext(), "onNext " + translation.content.out, 0).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
