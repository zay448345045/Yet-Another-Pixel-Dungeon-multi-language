/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.jinkeloid.yellowbananapixeldungeon.visuals.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.jinkeloid.yellowbananapixeldungeon.Dungeon;
import com.jinkeloid.yellowbananapixeldungeon.actors.hero.Hero;
import com.jinkeloid.yellowbananapixeldungeon.actors.mobs.npcs.Wandmaker;
import com.jinkeloid.yellowbananapixeldungeon.items.Item;
import com.jinkeloid.yellowbananapixeldungeon.items.wands.Wand;
import com.jinkeloid.yellowbananapixeldungeon.scenes.PixelScene;
import com.jinkeloid.yellowbananapixeldungeon.visuals.sprites.ItemSprite;
import com.jinkeloid.yellowbananapixeldungeon.visuals.ui.RedButton;
import com.jinkeloid.yellowbananapixeldungeon.visuals.ui.Window;
import com.jinkeloid.yellowbananapixeldungeon.misc.utils.GLog;
import com.jinkeloid.yellowbananapixeldungeon.misc.utils.Utils;

public class WndWandmaker extends Window {
	
	private static final String TXT_MESSAGE	= 
		"Oh, I see you have succeeded! I do hope it hasn't troubled you too much. " +
		"As I promised, you can choose one of my high quality wands.";
	
	private static final String TXT_FAREWELL = "Good luck in your quest, %s!";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public WndWandmaker( final Wandmaker wandmaker, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), null ) );
		titlebar.label( Utils.capitalize( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline message = PixelScene.createMultiline( TXT_MESSAGE, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnBattle = new RedButton( Wandmaker.Quest.wand1.name() ) {
			@Override
			protected void onClick() {
				selectReward( wandmaker, item, Wandmaker.Quest.wand1 );
			}
		};
		btnBattle.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnBattle );
		
		RedButton btnNonBattle = new RedButton( Wandmaker.Quest.wand2.name() ) {
			@Override
			protected void onClick() {
				selectReward( wandmaker, item, Wandmaker.Quest.wand2 );
			}
		};
		btnNonBattle.setRect( 0, btnBattle.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNonBattle );
		
		resize( WIDTH, (int)btnNonBattle.bottom() );
	}
	
	private void selectReward( Wandmaker wandmaker, Item item, Wand reward ) {
		
		hide();
		
		item.detach( Dungeon.hero.belongings.backpack );

		reward.identify();
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, wandmaker.pos ).sprite.drop();
		}
		
		wandmaker.yell( Utils.format( TXT_FAREWELL, Dungeon.hero.className() ) );
		wandmaker.destroy();
		
		wandmaker.sprite.die();
		
		Wandmaker.Quest.complete();
	}
}
