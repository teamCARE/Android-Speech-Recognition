package com.maxwell.speechrecognition;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Maxwell on 14-Jan-18.
 */

final class SpeechRecognitionListener implements RecognitionListener {

    private OnSpeechRecognitionListener onSpeechRecognitionListener;
    private Context context;

    SpeechRecognitionListener(OnSpeechRecognitionListener onSpeechRecognizerListener, Context context){
        this.onSpeechRecognitionListener = onSpeechRecognizerListener;
        this.context = context;
    }

    OnSpeechRecognitionListener getOnSpeechRecognitionListener(){
        return onSpeechRecognitionListener;
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {}

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onEndOfSpeech() {}

    @Override
    public void onError(int i) {

        String errorMessage = "";
        int errorCode = -1;

        switch (i) {
            case SpeechRecognizer.ERROR_AUDIO:
                errorCode = SpeechRecognizer.ERROR_AUDIO;
                errorMessage = context.getString(R.string.error_audio);
                break;

            case SpeechRecognizer.ERROR_CLIENT:
                errorCode = SpeechRecognizer.ERROR_CLIENT;
                errorMessage = context.getString(R.string.error_client);
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorCode = SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS;
                errorMessage = context.getString(R.string.error_permission);
                break;

            case SpeechRecognizer.ERROR_NETWORK:
                errorCode = SpeechRecognizer.ERROR_NETWORK;
                errorMessage = context.getString(R.string.error_network);
                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorCode = SpeechRecognizer.ERROR_NETWORK_TIMEOUT;
                errorMessage = context.getString(R.string.error_network);
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:
                errorCode = SpeechRecognizer.ERROR_NO_MATCH;
                errorMessage = context.getString(R.string.error_no_match);
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorCode = SpeechRecognizer.ERROR_RECOGNIZER_BUSY;
                errorMessage = context.getString(R.string.error_recognizer_busy);
                break;

            case SpeechRecognizer.ERROR_SERVER:
                errorCode = SpeechRecognizer.ERROR_SERVER;
                errorMessage = context.getString(R.string.error_server);
                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorCode = SpeechRecognizer.ERROR_SPEECH_TIMEOUT;
                errorMessage = context.getString(R.string.error_no_input);
                break;

            default:
                errorMessage = context.getString(R.string.error_undefined);
                break;
        }

        onSpeechRecognitionListener.OnSpeechRecognitionError(errorCode, errorMessage);
    }

    @Override
    public void onResults(Bundle bundle) {    //modified by teamCARE to return confidence levels

        float confidence;
        //sentence with highest confidence score is in position 0
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if(matches != null && matches.size() > 0){
            String sentence = matches.get(0);

            Log.i(SpeechRecognitionListener.class.getSimpleName(), sentence);
            //modified by teamCARE
            if (bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)!=null && bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES).length > 0) {  //avoids null ptr exception
                confidence = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0];
            } else {
                confidence = 0;
            }
            onSpeechRecognitionListener.OnSpeechRecognitionFinalResult(sentence, confidence);

        }else onError(SpeechRecognizer.ERROR_NO_MATCH);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        //sentence with highest confidence score is in position 0
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if(matches != null && matches.size() > 0){
            String word = matches.get(0);

            Log.i(SpeechRecognitionListener.class.getSimpleName(), word);
            onSpeechRecognitionListener.OnSpeechRecognitionCurrentResult(word);


        }else onError(SpeechRecognizer.ERROR_NO_MATCH);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {}


    //MARK: modified by teamCARE
    @Override
    public void onRmsChanged(float v) {
        onSpeechRecognitionListener.OnSpeechRecognitionRmsChanged(v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        onSpeechRecognitionListener.OnBufferReceived(bytes);
    }

    public void OnAudioReceived(Uri audioUri){
        onSpeechRecognitionListener.OnAudioReceived(audioUri);
    }

}
