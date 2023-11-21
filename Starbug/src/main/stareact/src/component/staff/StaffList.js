import {Link} from 'react-router-dom';

import {useState,useEffect} from'react';

import axios from 'axios';
import Header from '../Header.js';
import style from '../member/Member.css';
//import style from '../order/Sale.css';

import StaffRegist from'./StaffRegist.js';
import TaaList from'./TaaList.js';
import Salary from'./Salary.js';
import StaffInfo from './StaffInfo.js';



export default function StaffList(){


    let [ viewer , setViewer ] = useState(1);
    const vChange = e=> {viewer=1; setViewer(viewer)}
    const vChange2 = e=> {viewer=2; setViewer(viewer)}
    const vChange3 = e=> {viewer=3; setViewer(viewer)}
    const vChange4 = e=> {viewer=4; setViewer(viewer)}

    useEffect( ()=> {addSelectCss()} , [viewer])


  function addSelectCss(){ // 카테고리클릭시 css 변경
    let sinfobtn = document.querySelector('.sinfobtn');
    let sregistbtn = document.querySelector('.sregistbtn');
    let staabtn = document.querySelector('.staabtn');
    let ssalary = document.querySelector('.ssalary');

    if (viewer==1) {
        sinfobtn.classList.add('stab');
        sregistbtn.classList.remove('stab');
        staabtn.classList.remove('stab');
        ssalary.classList.remove('stab');
    } else if(viewer==2){
        sinfobtn.classList.remove('stab');
        sregistbtn.classList.add('stab');
        staabtn.classList.remove('stab');
        ssalary.classList.remove('stab');
    } else if(viewer==3){
        sinfobtn.classList.remove('stab');
        sregistbtn.classList.remove('stab');
        staabtn.classList.add('stab');
        ssalary.classList.remove('stab');
    }else if(viewer==4){
        sinfobtn.classList.remove('stab');
        sregistbtn.classList.remove('stab');
        staabtn.classList.remove('stab');
        ssalary.classList.add('stab');

    }
  }



    return(<>

        <div className="bodyContainer">
           <div className="memberContainer">

                <div className="categoryArea">
                       <button type="button" className="sinfobtn" onClick={vChange}> 인사정보 </button>
                        <button type="button" className="sregistbtn" onClick={vChange2}> 인사등록 </button>
                        <button type="button" className="staabtn" onClick={vChange3}> 급여등록 </button>
                        <button type="button" className="ssalary" onClick={vChange4}> 근태등록 </button>

               {viewer == 1 ? <StaffInfo /> : viewer == 2 ? <StaffRegist /> :
                viewer == 3 ? <Salary />: viewer == 4 ? <TaaList /> :<StaffInfo />}


                </div>{/*categoryArea*/}
            </div>{/*memberContainer*/}
         </div>{/*bodyContainer*/}

    </>)
}






