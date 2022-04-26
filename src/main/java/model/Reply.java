package model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class Reply {

		private int reply_num;
		private int board_num;
		private String nickname;
		private Date regdate;
		private String content;
		private String regdate2;
		
		
		public Reply() {}


		public Reply(int reply_num, int board_num, String nickname, Date regdate, String content) {
			super();
			this.reply_num = reply_num;
			this.board_num = board_num;
			this.nickname = nickname;
			this.regdate = regdate;
			this.content = content;
		}


		
}
