import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';



// 1.  만든  컴포넌트(jsx파일내 함수) 호출
import Index from'./component/Index.js';

// mui 알람창 임폴트
import { SnackbarProvider } from 'notistack';


// 2. index.html 에 <div> DOM객체 호출
const root = ReactDOM.createRoot(document.getElementById('root'));
// 3. 리액트 랜더링 ( JSX -> HTML 변환 )
root.render(
    <SnackbarProvider maxSnack={ 3 }>
        <Index />
    </SnackbarProvider>
);




// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

