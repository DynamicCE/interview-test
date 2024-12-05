import { useState } from 'react'
import axios from 'axios'
import './App.css'

interface Question {
    question: string;
    options: string[];
    correctAnswer: string;
    explanation: string;
}

interface TestResult {
    score: number;
    weakTopics: string[];
    feedback: string;
}

function App() {
    const [category, setCategory] = useState('')
    const [questions, setQuestions] = useState<Question[]>([])
    const [answers, setAnswers] = useState<{[key: number]: string}>({})
    const [testId, setTestId] = useState<string>('')
    const [result, setResult] = useState<TestResult | null>(null)

    const handleStartTest = async () => {
        try {
            const response = await axios.get(`/api/test/start/${category}`)
            setQuestions(response.data.questions)
            setTestId(response.data.testId)
            setAnswers({})
            setResult(null)
        } catch (error) {
            console.error('Error fetching questions:', error)
            alert('Bir hata oluştu, lütfen tekrar deneyin.')
        }
    }

    const handleAnswerSelect = (questionIndex: number, optionIndex: string) => {
        setAnswers(prev => ({
            ...prev,
            [questionIndex]: optionIndex
        }))
    }

    const handleSubmit = async () => {
        if (Object.keys(answers).length !== questions.length) {
            alert('Lütfen tüm soruları cevaplayın!')
            return
        }

        try {
            const response = await axios.post('/api/test/submit', {
                testId: testId,
                category: category,
                answers: Object.values(answers),
                weakTopics: []
            })
            setResult(response.data)
        } catch (error) {
            console.error('Error submitting test:', error)
            alert('Testi gönderirken bir hata oluştu, lütfen tekrar deneyin.')
        }
    }

    return (
        <div className="App">
            <h1>Test Başlat</h1>
            <select value={category} onChange={(e) => setCategory(e.target.value)}>
                <option value="">Kategori Seç</option>
                <option value="CORE_JAVA">Core Java</option>
                <option value="SPRING_FRAMEWORK">Spring Framework</option>
                <option value="SQL">SQL</option>
                <option value="DESIGN_PATTERNS">Design Patterns</option>
            </select>
            <button onClick={handleStartTest}>Başlat</button>
            
            {questions.length > 0 && (
                <>
                    <div className="questions">
                        {questions.map((q, qIndex) => (
                            <div key={qIndex} className="question">
                                <h3>{q.question}</h3>
                                <ul>
                                    {q.options.map((option, optIndex) => (
                                        <li 
                                            key={optIndex} 
                                            className={`
                                                ${answers[qIndex] === String.fromCharCode(65 + optIndex) ? 'selected' : ''}
                                                ${result ? 
                                                    (String.fromCharCode(65 + optIndex) === q.correctAnswer ? 'correct' : 
                                                    answers[qIndex] === String.fromCharCode(65 + optIndex) ? 'wrong' : '') 
                                                    : ''
                                                }
                                            `}
                                            onClick={() => !result && handleAnswerSelect(qIndex, String.fromCharCode(65 + optIndex))}
                                        >
                                            {String.fromCharCode(65 + optIndex)}. {option}
                                        </li>
                                    ))}
                                </ul>
                                {result && (
                                    <div className="explanation">
                                        <p>{q.explanation}</p>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                    {!result && <button onClick={handleSubmit} className="submit-btn">Testi Bitir</button>}
                    {result && (
                        <div className="result">
                            <h2>Test Sonucu</h2>
                            <p>Puan: {result.score}</p>
                            <p>{result.feedback}</p>
                            {result.weakTopics.length > 0 && (
                                <div className="weak-topics">
                                    <h3>Çalışmanız Gereken Konular:</h3>
                                    <ul>
                                        {result.weakTopics.map((topic, index) => (
                                            <li key={index}>{topic}</li>
                                        ))}
                                    </ul>
                                </div>
                            )}
                        </div>
                    )}
                </>
            )}
        </div>
    )
}

export default App
