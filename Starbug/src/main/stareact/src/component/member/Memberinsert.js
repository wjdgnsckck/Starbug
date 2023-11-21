import { useState, useEffect } from 'react';
import axios from 'axios';
import style from './Memberinsert.css';

export default function MemberInsert(props){

    const memberSignup = (e)=>{
        console.log(e);
        let mnameInput = document.querySelector('.mname').value;
        let mphoneInput = document.querySelector('.mphone').value;
        let mageInput = document.querySelector('.mage').value;
        let msexInput = document.querySelector('.msex').value;
        let metcInput = document.querySelector('.metc').value;
        if (mnameInput=='' || mphoneInput=='' || mageInput==''
            || msexInput=='' || metcInput==''){
            alert('빈칸 없이 입력해주세요');
            return;
        }
        let info = {
            mname : mnameInput ,
            mphone : mphoneInput ,
            mage : mageInput ,
            msex : msexInput ,
            metc : metcInput
        };

        console.log(info);

        axios
            .post('http://localhost:80/member/post' , info )
            .then( r => {
                if(r.data) {
                    alert('회원가입 성공');
                    window.location.href='/member?headerCategory=3';
                } else {
                    alert('회원가입 실패');
                }
            });

    }

    return(<>

        <div className="memberinsertPage">
            <div className="memberinsertArea">
                <div className="minsertTitle">회원등록 양식</div>
                <input type="text" className="mname" placeholder="이름" />
                    <input type="text" className="mphone" placeholder="연락처" />
                    <select className="mage">
                        <option disabled>연령대</option>
                        <option value="10">10대</option>
                        <option value="20">20대</option>
                        <option value="30">30대</option>
                        <option value="40">40대</option>
                        <option value="50">50대</option>
                        <option value="60">60대이상</option>
                    </select>
                    <select className="msex">
                       <option disabled>성별</option>
                       <option value="남">남</option>
                       <option value="여">여</option>
                    </select>
                    <textarea type="text" className="metc" placeholder="특이사항"></textarea>
                    <button type="button" className="btnSignup" onClick={memberSignup}>회원 등록</button>
            </div>
            <div className="minfoArea">
                <div className="minfoTitle">회원등록시 주의사항</div>
                <div className="minfoContent">
                    <p>회원 등록은 빈칸이 없어야합니다.</p>
                    <p>회원에게 이름과 연락처만 받습니다.</p>
                    <p>연락처는 "010-0000-0000" 형식을 지켜서 작성해주세요.</p>
                    <p>연령대와 성별은 회원에게 묻지말고 등록해주세요.</p>
                    <p>특이사항이 없을 시 "없음"으로 작성해주세요.</p>
                </div>
            </div>
        </div>



    </>)
}

