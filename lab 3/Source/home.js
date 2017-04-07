/**
 * Created by akhila on 2/6/2017.
 */
var SpeechRecognition = SpeechRecognition || webkitSpeechRecognition;
var SpeechGrammarList = SpeechGrammarList || webkitSpeechGrammarList;
var SpeechRecognitionEvent = SpeechRecognitionEvent || webkitSpeechRecognitionEvent;

var phrases = [

    'enjoying',
    'How are you',
    'I am very happy',
    'I am so hungry',
    'Excitement',
    'I love my life',
    'I am depressed',
    'I hate you',
    'I enjoy my life'

]

var phrasePara = document.querySelector('.phrase');
var resultPara = document.querySelector('.result');
var diagnosticPara = document.querySelector('.output');

var testBtn = document.querySelector('button');

function randomPhrase() {
    var number = Math.floor(Math.random() * phrases.length);
    return number;
}

function testSpeech() {
    testBtn.disabled = true;
    testBtn.textContent = 'Test in progress';

    var phrase = phrases[randomPhrase()];
    phrasePara.textContent = phrase;
    resultPara.textContent = 'Right or wrong?';
    resultPara.style.background = 'rgba(0,0,0,0.2)';
    diagnosticPara.textContent = '...diagnostic messages';

    var grammar = '#JSGF V1.0; grammar phrase; public <phrase> = ' + phrase +';';
    var recognition = new SpeechRecognition();
    var speechRecognitionList = new SpeechGrammarList();
    speechRecognitionList.addFromString(grammar, 1);
    recognition.grammars = speechRecognitionList;
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    recognition.start();

    recognition.onresult = function(event) {

        var speechResult = event.results[0][0].transcript;
        diagnosticPara.textContent = 'Speech received: ' + speechResult + '.';
        if(speechResult === phrase) {
            resultPara.textContent = 'I heard the correct phrase!';
            resultPara.style.background = 'green';
        } else {
            resultPara.textContent = 'That didn\'t sound right.';
            resultPara.style.background = 'black';
        }

        console.log('Confidence: ' + event.results[0][0].confidence);
    }

    recognition.onspeechend = function() {
        recognition.stop();
        testBtn.disabled = false;
        testBtn.textContent = 'Start new test';
    }

    recognition.onerror = function(event) {
        testBtn.disabled = false;
        testBtn.textContent = 'Start new test';
        diagnosticPara.textContent = 'Error occurred in recognition: ' + event.error;
    }

}

testBtn.addEventListener('click', testSpeech);

var app=angular.module("Sentiment",[]);
app.controller("analyze",function ($scope,$http) {
    $scope.sentimentfinder=function () {
        var text1=$scope.findSentimentF;
        var texts=$http.get("http://gateway-a.watsonplatform.net/calls/text/TextGetTextSentiment?apikey=01d04b63837dc950966f8e775b838ffafadcae3c&outputMode=json&text=" +diagnosticPara.textContent )
        texts.success(function (response) {

            console.log(response);
            $scope.SentimentFU={"sentiment":response.docSentiment.type,"score":response.docSentiment.score};
            document.getElementById('list').style.display= 'block';
        });

    }

});