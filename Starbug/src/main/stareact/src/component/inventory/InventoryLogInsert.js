import styles from './InventoryLogInsert.css';
import axios from 'axios';
import{ useState , useEffect} from 'react'
/*mui 페이징처리*/
import Pagination from '@mui/material/Pagination';
/*mui 테이블*/
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
/*엑셀*/
import excelLogo from '../img/엑셀_로고.png';

export default function InventoryLogInsert(props){

   let [ rows , setRows ] = useState (                                      //초기출력 를 위한 useState
     {inventoryCount : [] , totalPage : 0 , totalCount : 0});               //초기 출력을 위해 값을 넣어준다!(반환받는pageDto의 리스트 , 총페이지 , 총 개수)

     let [ pageInfo , setPageInfo ] = useState ({                           //페이징처리를 위한 useState
     page : 1  , keyword : '' ,  serachState : false                    //값에 맞게 넣어준다(페이지번호,검색키워드,검색상태)
      })

   let [ logDate , setLogDate ] = useState(0);                      // excel에 날짜출력해주기위한 코드

   let [ existlog , setExistlog ] = useState(false);                 // excel

     const getLog = (e) =>{                                       // spring에 axios 사용을 위한 함수!
       axios.get('/inventory/printLog',{params : pageInfo})            // get 방식으로 페이지의 정보를 보내준다.
               .then( r =>{console.log(r.data)                         // Page<>
               setRows(r.data);}
               )
     }
       useEffect( ()=>{  getLog();  } , [ pageInfo.page , pageInfo.serachState,existlog] ); //page,view,serachState에 변화가 있을때! 재 랜더링 한다.


       const onPageSelect = ( e , value ) =>{                                           //page클릭시 !
       pageInfo.page =value;                                                            //page의 값 value 를 pageInfo.page에 저장
       setPageInfo({...pageInfo});                                                      // 다시 받은 객체를 주소값 초기화 해서 넣어준다! (이유는 주소값 변경시 재랜더링 실행하기 때문)
       }

        const onSearch = ( e ) =>{                                                      //검색 함수
        if (!pageInfo.keyword.trim()) {
                     alert('검색어를 입력해주세요.');
                   }
                   else {setPageInfo( { ...pageInfo  , page : 1 ,serachState:true} );         // 검색시 검색 했다는 serachState를 true로 바꾼다.
                        getLog();                                                            // 재 랜더링을 위해 getResoures를 호출한다.
                        }
           }
         const noSearch = (e) => {                                                     //검색삭제 클릭히 함수
           setPageInfo( { ...pageInfo , page : 1 , serachState:false,keyword:''});     //검색 삭제시 초기 페이지를 가기위해 값을 넣어준다.
           getLog();                                                       //재 랜더링을 위해 getResoures를 호출한다.
         }

         const saveDate = (e) =>{   console.log(e.target.value);
         axios.get('/inventory/existlog',{params : {date : e.target.value}})
         .then((r)=>{console.log(r.data) ; if(r.data){setExistlog(r.data)}
         else {setExistlog(r.data)}})
         setLogDate(e.target.value);
         }

         const insertInventoryLog = (e) => {
               let logForm = document.querySelectorAll('.logForm')[0];
                 console.log(logForm)
                  let logFormData = new FormData(logForm);
                 if (logFormData.get('inloghistory').trim() === '') {
                   alert('재료 로그 개수를 입력해주세요.');
                   return;
                 }

                 if (logFormData.get('resname').trim() === '') {
                   alert('재료 이름을 입력해주세요.');
                   return;
                 }
                  axios.post('/inventory/loginsert',logFormData)
                  .then( (r)=>{ console.log(r.data);                //값이 true 일때
                  if(r.data){alert("등록완료");                      //등록완료 알람
                  setPageInfo({ ...pageInfo, page: 1 });            //등록시페이지초기화
                  getLog();}
                  else if(r.data==2){alert("등록오류.")}}
               )
             }


         let [ todayParcelLog , setTodayParcelLog ] = useState( [] );

             useEffect( () => {
                 axios.get('/parcel/todaygetpa')
                 .then( (r) => {
                     setTodayParcelLog(r.data);
                 })
             } , [])

             // 모달창 띄우기
             let [ parcleLogState , setParcleLogState ] = useState( false );
             const parcelLogChack = (e) =>{
                 setParcleLogState( e );
             }
             // 오늘 날짜 구하는 함수 -> 모달 창에서 비교해서 출력해야함
             const today = new Date(); // 오늘 날짜
             const year = today.getFullYear(); // 오늘 날짜에서 년도 뺴오기
             const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더하고, 두 자리로 만듦
             const day = String(today.getDate()).padStart(2, '0'); // 날짜를 두 자리로 만듦




             const localdate = `${year}-${month}-${day}`;

             const logDelete  = (e)  => {
                        axios.delete("/inventory/logdelete",{ params: { inlogno : e }})
                        .then ( r=>{console.log(r.data);
                        if(r.data){alert("삭제완료");                       //true 반환시
                        setPageInfo({ ...pageInfo, page: 1 });             // 페이지 초기화
                        getLog();      }                                    //초기화를 위한 함수 실행
                        else{alert("삭제오류")}

                        });

             }
             //날짜 출력용 함수
               const formatDate = (dateString) => {
               const inlogDate = new Date(dateString);
               const now = new Date();
               const timeDifference = now - inlogDate;          // 현재 시간과의 전체 차이(milliseconds) 계산
               const secondsDifference = Math.floor(timeDifference / 1000);  // 밀리초(ms) 단위 차이를 초(s)로 변환
               const minutesDifference = Math.floor(secondsDifference / 60);        // 초(s) 단위 차이를 분(m)으로 변환
               const hoursDifference = Math.floor(minutesDifference / 60);    // 분(m) 단위 차이를 시간(h)으로 변환
               if (hoursDifference < 1) {
                   return `${minutesDifference}분 전`;
                 } else if (hoursDifference < 24) {
                   // 1시간 이상 24시간 이내의 경우 'x시간 전'으로 표시
                   return `${hoursDifference}시간 전`;
                 } else {
                  // 24시간 이상일 때는 해당 날짜의 년, 월, 일, 시, 분을 표시
                 const year = inlogDate.getFullYear();
                 const month = inlogDate.getMonth() + 1;
                 const day = inlogDate.getDate();
                 const hours = inlogDate.getHours();
                 const minutes = inlogDate.getMinutes();

                 return `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
               }
             };

       return(<>
           {  /*  예지 : bodyContainer DIV로 전체 틀의 크기 잡아놨습니다.  */ }
           <div className="inventoryWrap">
               <div className="insertTop">
                   <div className="pageComment">재고(재료) 로그</div>
                  <div className="excel">
                    <p>Inventory_Log Excel</p>
                    <input className="logDate" type="date" onInput={saveDate} />
                    { existlog ?
                    <a href={"/excel/logExcel?logdate="+logDate}><img src={excelLogo} className="excelImg" /></a>
                    : <> </>
                    }
                  </div>
               </div>

               <div className="inventoryContent">
                   <TableContainer component={Paper}>
                                     <Table className="logtable" sx={{ minWidth: 1100 }} aria-label="simple table" >
                                     {/* 테이블 제목 구역 */}
                                       <TableHead className="inventoryHeader">
                                         <TableRow>
                                           <TableCell align="center"> 재고 로그 번호</TableCell>
                                           <TableCell align="center"> 재고 로그 날짜</TableCell>
                                           <TableCell align="center"> 재고 로그 개수</TableCell>
                                           <TableCell align="center"> 재료 번호</TableCell>
                                           <TableCell align="center"> 재료 이름</TableCell>
                                           <TableCell align="center"> 비고 </TableCell>
                                         </TableRow>
                                       </TableHead>
                                       {/* 테이블 내용 구역 */}
                                       <TableBody className="inventoryBody">
                                          {rows.inventoryCount.map ((row) => (
                                           <TableRow key={row.name} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                             <TableCell  align="center"> {row.inlogno} </TableCell>
                                             <TableCell  align="center"> {formatDate(row.inlogdate)} </TableCell>
                                             <TableCell  align="center"> {row.inloghistory} </TableCell>
                                             <TableCell  align="center"> {row.resno} </TableCell>
                                             <TableCell  align="center"> {row.resname} </TableCell>
                                             <TableCell  align="center"> <button onClick={() => logDelete(row.inlogno)}  className="delete" type="button">삭제</button> </TableCell>
                                           </TableRow>
                                         ))}
                                       </TableBody>
                                     </Table>
                                   </TableContainer>
               </div>
                {/*페이징 처리 구역*/}
                    <div className="pagewraps">
                       <div className="page">
                       <Pagination page ={pageInfo.page} count={ rows.totalPage } onChange={onPageSelect} color="primary" />
                       </div>
                   {/* 검색 기능*/}
                       <div className="search">
                                       <input type="text" placeholder="재료이름을 입력해주세요" maxlength ="8"
                                        onChange = {
                                              (e)=>{ setPageInfo( { ...pageInfo , keyword : e.target.value } )  }
                                                       }/>
                                   <button type="button" onClick={ onSearch }>검색 </button>
                                   {
                                      pageInfo.serachState ?(<>
                                      <div className="searchDelete">
                                      <button type="button" onClick ={noSearch}>검색제거</button>
                                      </div></>) : (<></>)
                                   }
                       </div>
                       {/*<input type = "text"  onChange={serachPage} placeholder="볼 페이지 번호"/>*/}
                   </div>
                       <div className="inventoryBottom">
                            <form className="logForm">
                               <div className="inloghistory"><p>재고 로그 개수</p> <input name="inloghistory" type="text" maxlength ="3"
                               placeholder="재고 로그 개수"  pattern="[0-9]*"/></div>
                               <div className="resname"><p>재료 이름</p> <input name="resname" type="text"  placeholder="재료 이름"/></div>
                                <button type="button" onClick={()=>parcelLogChack(true)}>발주확인</button>
                               <button onClick={insertInventoryLog} type="button">등록하기</button>
                           </form>
                       </div>


            { parcleLogState == true ?
                    <>
                        <div className="parcelLogModal">
                            <div className="ModalTop">
                            { todayParcelLog[0].cdate.slice( 0 , 10 ) ==
                               localdate ? "오늘의 발주 내역" :
                                todayParcelLog[0].cdate.slice( 0 , 10 ) + "의 발주 내역"
                            }
                            </div>
                            <table>
                                <tr>
                                    <th>재료명</th>
                                    <th>발주개수</th>
                                    <th>상태</th>
                                </tr>
                                { todayParcelLog.map((r)=>{
                                    return(<>
                                        <tr>
                                            <td>{r.resname}</td>{/*재료이름*/}
                                            <td>{r.pacount}개</td>{/*발주개수*/}
                                            <td>{ r.cdate == r.udate ? "배송중" :
                                                r.pastate == true ? "환불 요청중" : "도착완료" }
                                            </td>{/*
                                                주문일 도착일 같으면 배송중
                                                pastate true 면 환불 요청 상태
                                                pastate false 고 주문일 도착일 다르면 도착 완료
                                                */}
                                        </tr>
                                    </>)
                                }) }
                            </table>
                            <div className="click">
                                 <button type="button" onClick={ ()=>parcelLogChack(false)}>닫기</button>
                            </div>
                        </div>
                    </> : <></>
                    }{/*발주내역 확인 모달구역 end*/}

           </div>{/*div wrap end*/}
       </>)

}