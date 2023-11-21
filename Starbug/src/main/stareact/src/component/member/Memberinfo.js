
import { Link , useSearchParams} from 'react-router-dom';
import axios from 'axios';
import { useState, useEffect } from 'react';
import style from './Memberinfo.css';

export default function MemberInfo( props ){
    console.log('실행');
    // 1.HTTP 경로상의 쿼리스트링 매개변수 호출
    const [ searchParams, setSearchParams ] = useSearchParams();
    const mno = searchParams.get('mno');
    //console.log("회원번호 :"+mno)
    let [ member , setMember ] = useState([ ]); // 회원 정보 담아둘 상태 변수 선언

    //console.log(member);

    // 회원정보 1명 출력
     useEffect(()=>{ // 컴포넌트가 생성될때 1번 되는 axios
          axios.get('/member/getinfo' , {params : {mno : mno}})
                .then(r=>{
                    setMember(r.data); // 응답받은 모든 게시물을 상태변수에 저장
                    console.log(r.data);
                    //console.log(rows);
                });
    } , []);

    // 날짜 형식 변환 함수
    function formatDate(dateString){
          let date = new Date(dateString);
          let year = date.getFullYear();
          let month = date.getMonth() + 1; // 월은 0부터 시작하므로 1을 더합니다.
          let day = date.getDate();
          return `${year}년 ${month}월 ${day}일`;
    }

    // 회원 수정 함수
    const memberEdit = (e) =>{
        console.log("수정실행");

         let newInfo = {
                    mno : member.mno ,  // 수정할 대상 / 주체
                    mname : member.mname ,  // 수정할 값
                    mphone : member.mphone ,
                    mage : member.mage ,
                    metc : member.metc
         };
        console.log(newInfo);

        axios.put('/member/put' , newInfo)
            .then( r=>{
                if(r.data){alert('회원정보 수정 성공');
                    window.location.href='/member?headerCategory=3';
                } else { alert('회원정보 수정 실패');}
            })
    }

    // 회원 삭제 함수
    const memberDelete = (e)=>{
        console.log("삭제실행");
        if(window.confirm('정말 삭제하시겠습니까? 회원을 삭제하면 복구 할 수 없습니다.')) {
            axios.delete('/member/delete' , {params : { mno : mno }} ) // 회원번호 보냄
                .then( r=>{
                    if(r.data){alert('회원이 삭제되었습니다.');
                        window.location.href='/member?headerCategory=3';
                    } else { alert('회원 삭제 실패');}
                })

        }
    }

    // 쿠폰 날짜 계산 함수
    function couponDate(cdate , daynum ){
        let date = new Date;    // 현재날짜
        //console.log(date);
        let newCdate = new Date(cdate);    // 쿠폰발급된 날짜
        //console.log(newCdate);
        let dateSum = newCdate.setDate(newCdate.getDate() + daynum);  // 쿠폰발급된 날짜 + 쿠폰사용가능일수
        return formatDate(dateSum);   //날짜 형식 변환 함수 호출 후 반환
    }

    return(<>

        <div className="memberInfoContainer">
            <div className="memberInfocontentArea">
                <div className="minfo_Title">회원 수정/삭제 페이지</div>
                <div className="rinkBack">
                    <a href="http://localhost/member?headerCategory=3"><span>←</span> 뒤로가기</a>
                </div>
                <form className="mcontentArea">
                     <div className="mcontentTitle">회원정보</div>
                     <div className="memberName">
                         <div> 이름 </div>
                         <input className="mnameInput" type="text" name="mname" value={member.mname}
                            onChange = { (e) => { setMember ({ ...member , mname : e.target.value } ) } }/>
                     </div>
                     <div className="memberInfocontent">
                         <div> 연락처 </div>
                         <input className="mphoneInput" type="text" name="mphone" value={member.mphone}
                            onChange = { (e) => { setMember ({ ...member , mphone : e.target.value } ) } } />
                     </div>
                     <div className="memberInfocontent">
                         <div> 연령대 </div>
                         <input className="mageInput" type="text" name="mage" value={member.mage}대
                            onChange = { (e) => { setMember ({ ...member , mage : e.target.value } ) } }/>
                     </div>
                     <div className="memberInfocontent">
                         <div> 성별</div>
                         <input className="msexInput" type="text" value={member.msex} disabled />
                     </div>
                     <div className="memberInfocontent">
                        <div>가입일 </div>
                        <input className="mdateInput" type="text" value={formatDate(member.mdate)} disabled/>
                     </div>
                     <div className="memberInfocontent">
                        <div> 특이사항 </div>
                        <textarea className="metcInput" defaultValue={member.metc}
                         onChange = { (e) => { setMember({ ...member, metc: e.target.value }) } } >

                        </textarea>
                     </div>

                     <div className="memberInfobtnArea">
                         <button type="button" className="btnMemberinfo" onClick={memberEdit}>수정</button>
                         <button type="button" className="btnMemberDelete" onClick={memberDelete}>삭제</button>
                     </div>

                </form>

                <div className="memberCouponArea">
                    <div className="ccontentTitle">회원 쿠폰정보</div>
                     <table className="couponTableArea">
                        <colgroup>
                            <col width="15%" />
                            <col width="35%" />
                            <col width="25%" />
                            <col width="25%" />
                        </colgroup>
                        <tr>
                            <th>쿠폰 번호</th>
                            <th>쿠폰 이름</th>
                            <th>쿠폰 기한</th>
                            <th>쿠폰 상태</th>
                        </tr>
                        {
                            member.couponHistoryDtos && member.couponHistoryDtos.map ((row)=>{
                                return(<>
                                    <tr className="memberTabletr">
                                        <td>{row.chisno}</td>
                                        <td>{row.cccontent}</td>
                                        <td>{couponDate(row.cdate , row.ccdate)}까지</td>
                                        <td>{row.chstate == 0 ? '사용가능' : '사용불가' }</td>
                                    </tr>
                                </>)
                            })
                        }
                     </table>

                </div>




            </div>
        </div>



    </>)
}