package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the group_users database table.
 * 
 */
@Entity
@Table(name="group_users")
@NamedQuery(name="GroupUser.findAll", query="SELECT g FROM GroupUser g")
public class GroupUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="`GROUP`")
	private int group;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER")
	private User userBean;

	public GroupUser() {
	}

	public int getGroup() {
		return this.group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public User getUserBean() {
		return this.userBean;
	}

	public void setUserBean(User userBean) {
		this.userBean = userBean;
	}

}