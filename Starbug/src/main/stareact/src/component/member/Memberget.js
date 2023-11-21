
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useState, useEffect } from 'react';
import style from './Member.css';
import Pagination from '@mui/material/Pagination';

export default function MemberGet( props ){
    // 모든 체크박스 상태 체크하기위한 상태변수
    let [checkedAll, setCheckedAll] = useState(false);

    const checkAllChecked = (e)=>{
        if (checkedAll==false){
            //console.log("상태체크 false일때");
            setCheckedAll(true); // 체크되면 상태변화
        } else {
            //console.log("상태체크 true일때");
            setCheckedAll(false);
        }
    }

    useEffect(()=>{ // 체크박스 상태 변할때마다 화면 재랜더링
        //console.log(checkedAll);
        let checkboxMember = document.querySelectorAll(".checkMember");
        if (checkedAll==true){  // 체크된 상태이면 모든 체크박스의 상태를 true로 변환
            for (let i=0; i<checkboxMember.length; i++){
                //console.log("상태true 포문돈다");
                document.querySelectorAll(".checkMember")[i].checked=true;
            }
        } else {
             for (let i=0; i<checkboxMember.length; i++){
                //console.log("상태false 포문돈다");
                document.querySelectorAll(".checkMember")[i].checked=false;
             }
        }
    } ,[checkedAll] );


    let [ coupons , setCoupons ] = useState({
        couponDtos : [] ,
        totalPage : 0 ,
        totalCount : 0});    // 쿠폰카테고리 담아둘 상태변수

    //console.log(coupons);

    // 페이징처리 위한 상태변수 전달받은객체
    let [ pageDto , setPageDto ] = useState({
        memberDtos : [] ,
        totalPage : 0 ,
        totalCount : 0
    });

    //console.log(pageDto);

    // 페이징처리 위한 페이지 번호 상태변수 전달할 객체
    const [ pageInfo , setPageInfo ] = useState({
        page : 1 , key : 'mname' , keyword : '' , view : 10 // view 초기값 10
    });
    //console.log(pageInfo);

     useEffect(()=>{ // 컴포넌트가 생성될때 1번 되는 axios

              // 쿠폰카테고리 호출
               axios.get('/coupon/getcc' , {params : {page : 1 , view : 100}})
                      .then ((r)=>{
                          if (r.data) { setCoupons(r.data);  // 쿠폰 데이터 업데이트
                          } else { console.log('호출실패');}
                      })
        } , []);


    // 회원정보 가져오는 함수
     const getMember = (e) => {
        // 회원정보 호출
        axios.get('/member/get' , {params : pageInfo})
             .then(r=>{
                //console.log(r.data);
                //console.log(r.data.membersDtos.memberDtos);
                //console.log(r.data.membersDtos);
                 setPageDto(r.data);
                 //console.log(pageDto);
             });
     }

    // 컴포넌트가 생성될때 1번 되는 axios
    useEffect(()=>{
        getMember();
        setCheckedAll(false);   // 새로불러오면 false로 상태변경
        document.querySelector('.checkMemberAll').checked = false;  // 체크박스 초기화
    } , [ pageInfo.page  , pageInfo.view ]);

    // 2. 페이지 번호를 클릭했을때
    const onPageSelect = (e , value ) =>{
        console.log(value);
        pageInfo.page = value; // 클릭한 페이지번호로 변경
        setPageInfo({ ...pageInfo });   // 새로고침 [ 상태변수의 주소값이 바뀌면 재랜더링 ]
    }


    // 3. 검색 버튼을 눌렀을때
    const onSearch = (e) =>{
        setPageInfo( {...pageInfo , page : 1 }); // 첫페이지 1페이지로 초기화
        setCheckedAll(false);   // 새로불러오면 false로 상태변경
        document.querySelector('.checkMemberAll').checked = false;  // 체크박스 초기화
        // 사용자가 체크한 상태에서 다시 검색을 할수도 있으므로 모든 체크박스를 초기화해준다.
        let checkboxMember = document.querySelectorAll(".checkMember");
        for (let i=0; i<checkboxMember.length; i++){
                    document.querySelectorAll(".checkMember")[i].checked=false;
        }
        setInputmCoupon({ ...inputmCoupon }) // 쿠폰에 담긴 회원도 초기화
        getMember(); // axios 실행
    }

    // axios 통신할 객체 상태변수
    let [ inputmCoupon, setInputmCoupon] = useState({
        couponnum : 0 ,
        mno : []
    });

    // 쿠폰발급 함수
    const addCoupon = (e) =>{
       // console.log('실행');
        const cpSelectElement = document.querySelector('.cpSelect');
        if (cpSelectElement) {
            inputmCoupon.couponnum = cpSelectElement.value; // 선택한 쿠폰을 couponnum에 추가
            let checkMember = document.querySelectorAll('.checkMember');

            checkMember.forEach((member , i) => {
                if (member.checked) {   // check 된 상태면
                    let checkMno = document.querySelectorAll('.checkMno')[i].innerHTML; // 회원번호를 담아둘 변수
                    inputmCoupon.mno.push(checkMno);    // inputmCoupon의 mno에 회원번호 저장
                   // console.log(inputmCoupon);
                }
            });

            axios
                 .post('/coupon/membercouponw' , { ccno: inputmCoupon.couponnum, mno: inputmCoupon.mno })
                 .then(r=>{
                     alert("쿠폰발급 성공");
                     window.location.href='/member?headerCategory=3';    // 기존페이지로 돌아감
                 });
        }
    }

    // 날짜 형식 변환 함수
    function formatDate(dateString){
          let date = new Date(dateString);
          let year = date.getFullYear();
          let month = date.getMonth() + 1; // 월은 0부터 시작하므로 1을 더합니다.
          let day = date.getDate();
          return `${year}년 ${month}월 ${day}일`;
    }

    // 말줄임표 함수
    function textShort(text, maxLength) {
      if (text.length > maxLength) {
        return text.substring(0, maxLength) + '...';
      } else {
        return text;
      }
    }


    return(<>
        <div className="memberbtnArea">
            <select className="cpSelect">
                <option value="0"disabled >발급할 쿠폰 종류</option>
                {
                     coupons.couponDtos && coupons.couponDtos.map((row)=>{
                            return(<>
                                <option value={row.ccno}>{row.cccontent}({row.ccdate}일)</option>
                            </>)
                    })
                }

            </select>
            <button type="button" className="cpInsert" onClick={addCoupon}>쿠폰 발급</button>
            <select className="msearchSelect" value={ pageInfo.key}
                onChange={ (e)=>{ setPageInfo({ ...pageInfo , key : e.target.value }) }}>
                <option value="mname">이름</option>
                <option value="mphone">연락처</option>
            </select>
            <input type="text" placeholder="회원 이름,전화번호 검색" className="findMember"
                value={pageInfo.keyword}
                onChange={
                    (e)=>{ setPageInfo({ ...pageInfo , keyword : e.target.value }) }
                }/>
            <button type="button" className="btnSearch" onClick={onSearch}>검색하기</button>
        </div>
        <div className="tableArea">
            <table>
                <tr>
                    <th>
                        <input type="checkbox" className="checkMemberAll" onClick={checkAllChecked}/>
                    </th>
                    <th className="mnoWidth">회원 번호</th>
                    <th>이름</th>
                    <th className="phoneWidth">전화 번호</th>
                    <th>가입일</th>
                    <th className="memoArea">메모</th>
                </tr>

                {
                    pageDto.membersDtos && pageDto.membersDtos.map((row) =>{
                    {/* 리액트는 랜더링 먼저 일어나고 그다음 기능이 실행된다. 조건이 참이면 && 바로 뒤의 요소가 출력에 나타난다. */}
                        return(<>
                            <tr className="memberTabletr" key={row.mno}>
                                <td><input type="checkbox" className="checkMember"/></td>
                                <td className='checkMno'>{row.mno}</td>
                                <td><Link to={"/memberinfo?mno="+row.mno}>{row.mname} </Link></td>
                                <td>{row.mphone}</td>
                                <td>{formatDate(row.mdate)}</td>
                                <td className="leftText">{textShort(row.metc , 20)}</td>
                            </tr>
                        </>)
                    })
                }

            </table>
            <Pagination count={pageDto.totalPage}  page={pageInfo.page} onChange={ onPageSelect } />
        </div>



    </>)
}